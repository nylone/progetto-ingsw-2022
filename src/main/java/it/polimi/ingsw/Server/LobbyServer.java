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

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Handler of game events, responsible for communication of game related information with a single client.
 */
public class LobbyServer implements Runnable{
    protected static final Map<UUID, Lobby> lobbyMap = new ConcurrentHashMap<>();
    private static final Set<String> connectedNicknames = new HashSet<>();
    private final SocketWrapper sw;
    private final BlockingQueue<ClientEvent> eventQueue;

    /**
     * Creates the server around a {@link SocketWrapper}
     * @param sw the socket to use to communicate with the client
     */
    private LobbyServer(SocketWrapper sw) {
        this.sw = sw;
        this.eventQueue = new ArrayBlockingQueue<>(10);
    }

    /**
     * generates a unique UUID for a lobby
     * @return a not yet in use UUID for the lobby
     */
    private static UUID generateUUID() {
        UUID id = UUID.randomUUID();
        while (lobbyMap.containsKey(id)) {
            id = UUID.randomUUID();
        }
        return id;
    }

    /**
     * Get the event queue this server listens on
     * @return the {@link BlockingQueue<ClientEvent>} linked to this server
     */
    private BlockingQueue<ClientEvent> getEventQueue() {
        return this.eventQueue;
    }

    /**
     * Start a server on the provided socket wrapper
     * @param socketWrapper the wrapped connection to the client of the server
     */
    public static void spawn(SocketWrapper socketWrapper) {
        LobbyServer lobbyServer = new LobbyServer(socketWrapper);
        new Thread(lobbyServer).start();
        SocketListener.subscribe(socketWrapper, lobbyServer.getEventQueue());
    }

    /**
     * When run, polls for events from the client and sends appropriate responses while handling interactions with the game
     */
    @Override
    public void run() {
        String nickname = "unknown";
        int playerID = -1;
        Lobby currentLobby = null;
        State state = State.ACCEPT_PHASE;
        while (true) {
            try {
                ClientEvent event = this.eventQueue.take();
                Logger.info("Lobby server received a new Event: " + event.getClass());
                if (event instanceof SocketClosedEvent) {
                    if (currentLobby != null) {
                        currentLobby.disconnectPlayer(nickname);
                    }
                    synchronized (connectedNicknames) {
                        connectedNicknames.remove(nickname);
                    }
                    Logger.info("Lobby server was closed for player: " +
                            nickname +
                            " on address " +
                            this.sw.getInetAddress());
                    return;
                } else {
                    switch (state) {
                        case ACCEPT_PHASE -> {
                            if (event instanceof DeclarePlayerRequest castedEvent) {
                                nickname = castedEvent.getNickname();
                                synchronized (connectedNicknames) {
                                    if (connectedNicknames.contains(nickname)) {
                                        sw.sendMessage(new LobbyAccept(StatusCode.Fail, null));
                                    } else {
                                        connectedNicknames.add(nickname);
                                        List<LobbyInfo> publicLobbies = lobbyMap.values().stream()
                                                .filter(Lobby::isPublic)
                                                .filter(Predicate.not(Lobby::isGameInProgress))
                                                .map(LobbyInfo::new)
                                                .toList();
                                        sw.sendMessage(new LobbyAccept(StatusCode.Success, publicLobbies));
                                        state = State.REDIRECT_PHASE;
                                    }
                                }
                            } else {
                                sw.sendMessage(new InvalidRequest());
                            }
                        }
                        case REDIRECT_PHASE -> {
                            // redirect phase: wait for valid lobby action
                            // either:
                            // - create
                            // - join or rejoin
                            switch (event) {
                                case CreateLobbyRequest castedEvent -> {
                                    if (castedEvent.getMaxPlayers() < 1 || castedEvent.getMaxPlayers() > 4) {
                                        sw.sendMessage(LobbyRedirect.fail());
                                        break;
                                    }
                                    UUID lobbyID = generateUUID();
                                    currentLobby = new Lobby(
                                            lobbyID,
                                            castedEvent.isPublic(),
                                            castedEvent.getMaxPlayers(),
                                            nickname
                                    );
                                    currentLobby.addPlayer(nickname, this.getEventQueue());
                                    lobbyMap.put(lobbyID, currentLobby);
                                    state = State.GAME_START_PHASE;
                                    sw.sendMessage(LobbyRedirect.success(lobbyID, currentLobby.getAdmin()));
                                }
                                case ConnectLobbyRequest castedEvent -> {
                                    UUID lobbyID = castedEvent.getCode();
                                    if (!lobbyMap.containsKey(lobbyID) || !lobbyMap.get(lobbyID).addPlayer(nickname, this.getEventQueue())) {
                                        sw.sendMessage(LobbyRedirect.fail());
                                        break;
                                    }
                                    currentLobby = lobbyMap.get(lobbyID);
                                    sw.sendMessage(LobbyRedirect.success(lobbyID, currentLobby.getAdmin()));
                                    state = State.GAME_START_PHASE;
                                }
                                case default -> sw.sendMessage(new InvalidRequest());
                            }
                        }
                        case GAME_START_PHASE -> {
                            // wait phase: wait for valid lobby action
                            // either:
                            // - start (only from admin)
                            // - start (as admin event reaction)
                            switch (event) {
                                case LobbyClosedEvent ignored -> {
                                    currentLobby = null;
                                    state = State.REDIRECT_PHASE;
                                    sw.sendMessage(new LobbyClosed());
                                }
                                case ClientConnectEvent clientConnectedEvent ->
                                        sw.sendMessage(new ClientConnected(clientConnectedEvent.getNickname(), clientConnectedEvent.getPlayers()));
                                case ClientDisconnectEvent clientDisconnectedEvent ->
                                        sw.sendMessage(new ClientDisconnected(clientDisconnectedEvent.getNickname(), clientDisconnectedEvent.getPlayers()));
                                case StartGameRequest castedEvent -> {
                                    if (!currentLobby.getAdmin().equals(nickname)) {
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
                                        currentLobby.startGame(castedEvent.getGameMode());
                                    } catch (InputValidationException e) {
                                        sw.sendMessage(GameInit.fail(e.getMessage()));
                                        return;
                                    }
                                    // code executes only when a gameLobby was created
                                    sw.sendMessage(GameInit.success());
                                }
                                case GameStartEvent gameStartEvent -> {
                                    state = State.GAME_IN_PROGRESS_PHASE;
                                    playerID = gameStartEvent.getNickToID().get(nickname);
                                    sw.sendMessage(new GameStarted());
                                }
                                case default -> sw.sendMessage(new InvalidRequest());
                            }
                        }
                        case GAME_IN_PROGRESS_PHASE -> {
                            // wait phase: wait for valid lobby action
                            // either:
                            // - start (only from admin)
                            // - start (as admin event reaction)
                            switch (event) {
                                case LobbyClosedEvent ignored -> {
                                    currentLobby = null;
                                    state = State.REDIRECT_PHASE;
                                    sw.sendMessage(new LobbyClosed());
                                }
                                case ModelUpdateEvent modelUpdateEvent -> sw.sendMessage(new ModelUpdated(modelUpdateEvent.getModel()));
                                case GameOverEvent gameOverEvent -> {
                                    sw.sendMessage(new GameOver(gameOverEvent.getWinners()));
                                    currentLobby.close();
                                }
                                case PlayerActionRequest playerActionRequest -> {
                                    try {
                                        PlayerAction pa = playerActionRequest.getAction();
                                        if (pa.getPlayerBoardID() == playerID) {
                                            PlayerActionFeedback feedback;
                                            try {
                                                currentLobby.executeAction(pa);
                                                feedback = PlayerActionFeedback.success();
                                            } catch (InputValidationException e) {
                                                feedback = PlayerActionFeedback.fail(e.getMessage());
                                            }
                                            sw.sendMessage(feedback);
                                        } else {
                                            sw.sendMessage(PlayerActionFeedback.fail("The action that was sent is malformed."));
                                        }
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
                    }
                }
            } catch (Exception e) {
                Logger.severe(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Represents the various stages of the connection between client and server
     */
    private enum State {
        /**
         * client has not identified itself yet
         */
        ACCEPT_PHASE,
        /**
         * client now has a name, but is not part of a lobby
         */
        REDIRECT_PHASE,
        /**
         * client is part of a lobby, waiting for the game to start
         */
        GAME_START_PHASE,
        /**
         * client is now in a game, playing
         */
        GAME_IN_PROGRESS_PHASE,
    }
}
