package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.Actions.PlayerAction;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Operation.OperationException;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.ClientEvent;
import it.polimi.ingsw.Server.Messages.Events.Internal.*;
import it.polimi.ingsw.Server.Messages.Events.Requests.*;
import it.polimi.ingsw.Server.Messages.ServerResponses.*;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.LobbyInfo;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class LobbyServer {
    protected static final Map<UUID, Lobby> lobbyMap = new ConcurrentHashMap<>();
    private static final Set<String> connectedNicknames = new HashSet<>();
    private final SocketWrapper sw;
    private final ClientEventHandler eventHandler;
    private String nickname;
    private int playerID;
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
                        synchronized (connectedNicknames) {
                            connectedNicknames.remove(this.nickname);
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
            synchronized (connectedNicknames) {
                if (connectedNicknames.contains(this.nickname)) {
                    sw.sendMessage(new LobbyAccept(StatusCode.Fail, null));
                } else {
                    connectedNicknames.add(this.nickname);
                    List<LobbyInfo> publicLobbies = lobbyMap.values().stream()
                            .filter(Lobby::isPublic)
                            .filter(Predicate.not(Lobby::isGameInProgress))
                            .map(LobbyInfo::new)
                            .toList();
                    sw.sendMessage(new LobbyAccept(StatusCode.Success, publicLobbies));
                    this.state = State.REDIRECT_PHASE;
                }
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
            case GameStartEvent gameStartEvent -> {
                this.state = State.GAME_IN_PROGRESS_PHASE;
                this.playerID = gameStartEvent.getNickToID().get(this.nickname);
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
                    if (pa.getPlayerBoardID() == this.playerID) {
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
