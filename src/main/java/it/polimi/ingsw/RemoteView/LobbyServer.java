package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.Controller.Actions.PlayerAction;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Operation.OperationException;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;
import it.polimi.ingsw.RemoteView.Messages.Events.Internal.*;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.*;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.*;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.LobbyInfo;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyServer {
    protected static final Map<UUID, Lobby> lobbyMap = new ConcurrentHashMap<>();
    private static final Map<String, String> nickToPass = new ConcurrentHashMap<>(); // maps username to password
    private final SocketWrapper sw;
    private final ClientEventHandler eventHandler;
    private String nickname;
    private Lobby currentLobby;
    private UUID currentLobbyID;
    private State state;

    private LobbyServer(SocketWrapper sw) {
        this.sw = sw;
        this.eventHandler = new ClientEventHandler();
        this.state = State.ACCEPT_PHASE;
        new Thread(() -> {
            while (true) {
                try {
                    ClientEvent event = this.eventHandler.dequeue();
                    Logger.info("Lobby server received a new Event: " + event.getClass());
                    if (event instanceof SocketClosedEvent) {
                        if (this.currentLobby != null) {
                            this.currentLobby.disconnectPlayer(this.nickname);
                        }
                        Logger.info("Lobby server was closed for player: " +
                                nickname +
                                " on address " +
                                this.sw.getInetAddress());
                        return;
                    } else {
                        switch (this.state) {
                            case ACCEPT_PHASE -> acceptPhase(event);
                            case REDIRECT_PHASE -> redirectPhase(event);
                            case GAME_START_PHASE -> gameStartPhase(event);
                            case GAME_IN_PROGRESS_PHASE -> gameInProgressPhase(event);
                        }
                    }
                } catch (Exception e) {
                    Logger.severe(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void acceptPhase(ClientEvent clientEvent) throws IOException {
        if (clientEvent instanceof DeclarePlayerRequest castedEvent) {
            this.nickname = castedEvent.getNickname();
            String password = castedEvent.getPassword();
            if (nickToPass.get(this.nickname) != null && !nickToPass.get(this.nickname).equals(password)) {
                sw.sendMessage(new LobbyAccept(StatusCode.Fail, null, null));
            } else {
                nickToPass.put(this.nickname, password);
                List<LobbyInfo> publicLobbies = lobbyMap.values().stream()
                        .filter(Lobby::isPublic)
                        .map(LobbyInfo::new)
                        .toList();
                List<LobbyInfo> lobbiesWaitingReconnection = lobbyMap.values().stream()
                        .filter(lobby -> lobby.getDisconnectedPlayers().contains(this.nickname))
                        .map(LobbyInfo::new)
                        .toList();
                sw.sendMessage(new LobbyAccept(StatusCode.Success, publicLobbies, lobbiesWaitingReconnection));
                this.state = State.REDIRECT_PHASE;
            }
        } else {
            sw.sendMessage(new InvalidRequest());
        }
    }

    private void redirectPhase(ClientEvent clientEvent) throws IOException {
        // redirect phase: wait for valid lobby action
        // either:
        // - create
        // - join or rejoin
        switch (clientEvent) {
            case CreateLobbyRequest castedEvent -> {
                if (castedEvent.getMaxPlayers() < 1 || castedEvent.getMaxPlayers() > 4) {
                    sw.sendMessage(LobbyRedirect.fail());
                    break;
                }
                this.currentLobbyID = generateUUID();
                this.currentLobby = new Lobby(
                        this.currentLobbyID,
                        castedEvent.isPublic(),
                        castedEvent.getMaxPlayers(),
                        nickname
                );
                this.currentLobby.addPlayer(nickname, this.getEventHandler());
                lobbyMap.put(this.currentLobbyID, this.currentLobby);
                this.state = State.GAME_START_PHASE;
                sw.sendMessage(LobbyRedirect.success(this.currentLobbyID, this.currentLobby.getAdmin()));
            }
            case ConnectLobbyRequest castedEvent -> {
                UUID id = castedEvent.getCode();
                if (!lobbyMap.containsKey(id) || !lobbyMap.get(id).addPlayer(nickname, this.getEventHandler())) {
                    sw.sendMessage(LobbyRedirect.fail());
                    break;
                }
                this.currentLobbyID = castedEvent.getCode();
                this.currentLobby = lobbyMap.get(id);
                sw.sendMessage(LobbyRedirect.success(id, this.currentLobby.getAdmin()));
                this.state = State.GAME_START_PHASE;
            }
            case default -> sw.sendMessage(new InvalidRequest());
        }
    }

    private void gameStartPhase(ClientEvent clientEvent) throws IOException {
        // wait phase: wait for valid lobby action
        // either:
        // - start (only from admin)
        // - start (as admin event reaction)
        switch (clientEvent) {
            case LobbyClosedEvent ignored -> {
                this.currentLobby = null;
                this.currentLobbyID = null;
                this.state = State.REDIRECT_PHASE;
                sw.sendMessage(new LobbyClosed());
            }
            case ClientConnectEvent clientConnectedEvent ->
                    sw.sendMessage(new ClientConnected(clientConnectedEvent.getNickname(), clientConnectedEvent.getPlayers()));
            case ClientDisconnectEvent clientDisconnectedEvent ->
                    sw.sendMessage(new ClientDisconnected(clientDisconnectedEvent.getNickname(), clientDisconnectedEvent.getPlayers()));
            case StartGameRequest castedEvent -> {
                if (!this.currentLobby.getAdmin().equals(this.nickname)) {
                    sw.sendMessage(GameInit.fail("Only the admin of the lobby can start the game."));
                    return;
                }
                if (!currentLobby.isLobbyFull()) {
                    sw.sendMessage(GameInit.fail("The lobby has not been filled"));
                    return;
                }
                if (currentLobby.isGameInProgress()) {
                    sw.sendMessage(GameInit.fail("The game has already started"));
                    return;
                }
                try {
                    this.currentLobby.startGame(castedEvent.getGameMode());
                } catch (InputValidationException e) {
                    sw.sendMessage(GameInit.fail(e.getMessage()));
                    return;
                }
                // code executes only when a gameLobby was created
                sw.sendMessage(GameInit.success());
            }
            case GameStartEvent ignored -> {
                this.state = State.GAME_IN_PROGRESS_PHASE;
                sw.sendMessage(new GameStarted());
            }
            case default -> sw.sendMessage(new InvalidRequest());
        }
    }

    private void gameInProgressPhase(ClientEvent clientEvent) throws IOException {
        // wait phase: wait for valid lobby action
        // either:
        // - start (only from admin)
        // - start (as admin event reaction)
        switch (clientEvent) {
            case LobbyClosedEvent ignored -> {
                this.currentLobby = null;
                this.currentLobbyID = null;
                this.state = State.REDIRECT_PHASE;
                sw.sendMessage(new LobbyClosed());
            }
            case ModelUpdateEvent modelUpdateEvent -> sw.sendMessage(new ModelUpdated(modelUpdateEvent.getModel()));
            case GameOverEvent gameOverEvent -> {
                sw.sendMessage(new GameOver(gameOverEvent.getWinners()));
                this.currentLobby.close();
            }
            case PlayerActionRequest playerActionRequest -> {
                try {
                    PlayerAction pa = playerActionRequest.getAction();
                    if (currentLobby.verifyPlayer(pa, this.nickname)) {
                        try {
                            this.currentLobby.executeAction(pa);
                        } catch (InputValidationException e) {
                            sw.sendMessage(PlayerActionFeedback.fail(e.getMessage()));
                        }
                    } else {
                        sw.sendMessage(PlayerActionFeedback.fail("The action that was sent is malformed."));
                    }
                    sw.sendMessage(PlayerActionFeedback.success());
                } catch (OperationException e) {
                    Logger.severe("Supposedly unreachable statement was reached:\n" + e.getMessage());
                    sw.sendMessage(PlayerActionFeedback.fail(e.getMessage()));
                    throw new RuntimeException(e);
                }
            }
            case ClientDisconnectEvent clientDisconnectedEvent ->
                    sw.sendMessage(new ClientDisconnected(clientDisconnectedEvent.getNickname(), clientDisconnectedEvent.getPlayers()));
            case default -> sw.sendMessage(new InvalidRequest());
        }
    }

    private static UUID generateUUID() {
        UUID id = UUID.randomUUID();
        while (lobbyMap.containsKey(id)) {
            id = UUID.randomUUID();
        }
        return id;
    }

    private ClientEventHandler getEventHandler() {
        return this.eventHandler;
    }

    public static void spawn(SocketWrapper socketWrapper) {
        LobbyServer lobbyServer = new LobbyServer(socketWrapper);
        SocketListener.subscribe(socketWrapper, lobbyServer.getEventHandler());
    }

    private enum State {
        ACCEPT_PHASE,
        REDIRECT_PHASE,
        GAME_START_PHASE,
        GAME_IN_PROGRESS_PHASE,
    }
}
