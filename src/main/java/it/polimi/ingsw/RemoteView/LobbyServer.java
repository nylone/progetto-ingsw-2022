package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.RemoteView.Messages.Events.*;
import it.polimi.ingsw.RemoteView.Messages.Events.Internal.*;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.*;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class LobbyServer {
    private static final Logger log = Logger.getAnonymousLogger();
    private static final Map<String, String> nickToPass = new ConcurrentHashMap<>(); // maps username to password
    private static final Map<UUID, Lobby> lobbyMap = new ConcurrentHashMap<>();
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
                    log.info("Lobby server received a new Event: " + event.getClass());
                    switch (event) {
                        case SocketClosedEvent ignored -> {
                            this.currentLobby.removePlayerHandler(this.nickname);
                            log.info("Lobby server was closed for player: " +
                                    nickname +
                                    " on address " +
                                    this.sw.getInetAddress());
                            return;
                        }
                        default -> {
                            switch (this.state) {
                                case ACCEPT_PHASE -> acceptPhase(event);
                                case REDIRECT_PHASE -> redirectPhase(event);
                                case GAME_START_PHASE -> gameStartPhase(event);
                                case GAME_IN_PROGRESS_PHASE -> gameInProgressPhase(event);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.severe(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void spawn(SocketWrapper socketWrapper) {
        LobbyServer lobbyServer = new LobbyServer(socketWrapper);
        SocketListener.subscribe(socketWrapper, lobbyServer.getEventHandler());
    }

    private ClientEventHandler getEventHandler() {
        return this.eventHandler;
    }

    private void acceptPhase(ClientEvent clientEvent) throws IOException {
        if (clientEvent instanceof DeclarePlayerRequest castedEvent) {
            this.nickname = castedEvent.getNickname();
            String password = castedEvent.getPassword();
            if (nickToPass.get(this.nickname) != null && !nickToPass.get(this.nickname).equals(password)) {
                sw.sendMessage(new LobbyAccept(StatusCode.Fail, null));
            } else {
                nickToPass.put(this.nickname, password);
                List<Pair<UUID, String>> openLobbies = lobbyMap.entrySet().stream()
                        .filter(e -> e.getValue().isPublic())
                        .map(e -> new Pair<>(e.getKey(), e.getValue().getAdmin()))
                        .toList();
                sw.sendMessage(new LobbyAccept(StatusCode.Success, openLobbies));
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
        // TODO - join started game
        switch (clientEvent) {
            case CreateLobbyRequest castedEvent -> {
                if (castedEvent.getMaxPlayers() < 1 || castedEvent.getMaxPlayers() > 4) {
                    sw.sendMessage(LobbyRedirect.fail());
                    break;
                }
                this.currentLobby = new Lobby(
                        castedEvent.isPublic(),
                        castedEvent.getMaxPlayers(),
                        nickname,
                        this.getEventHandler()
                );
                this.currentLobbyID = generateUUID();
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
            case ClientConnectEvent clientConnectedEvent -> sw.sendMessage(new ClientConnected(clientConnectedEvent.getNickname(), clientConnectedEvent.getNumberOfPlayersConnected()));
            case ClientDisconnectEvent clientDisconnectedEvent -> sw.sendMessage(new ClientDisconnected(clientDisconnectedEvent.getNickname()));
            case StartGameRequest castedEvent -> {
                if (!this.currentLobby.getAdmin().equals(this.nickname)) {
                    sw.sendMessage(GameInit.fail("Only the admin of the lobby can start the game."));
                    return;
                }
                if(!currentLobby.isLobbyFull()){
                    sw.sendMessage(GameInit.fail("The lobby has not been filled"));
                    return;
                }
                if(currentLobby.getGameHandler() != null){
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
            case ModelUpdateEvent modelUpdateEvent -> {
                GameBoard model = modelUpdateEvent.getModel();
                sw.sendMessage(new ModelUpdated(model));
            }
            case PlayerActionRequest playerActionRequest -> {
                try {
                    this.currentLobby.getGameHandler().executeAction(playerActionRequest.getAction());
                } catch (InputValidationException e) {
                    sw.sendMessage(PlayerActionFeedback.fail(e.getMessage()));
                } catch (ClassNotFoundException e) {
                    sw.sendMessage(PlayerActionFeedback.fail("The action that was sent could not be deserialized."));
                }
                sw.sendMessage(PlayerActionFeedback.success());
            }
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

    private enum State {
        ACCEPT_PHASE,
        REDIRECT_PHASE,
        GAME_START_PHASE,
        GAME_IN_PROGRESS_PHASE,
    }
}