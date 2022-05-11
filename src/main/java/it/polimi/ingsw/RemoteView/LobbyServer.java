package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.RemoteView.Messages.ClientEvents.*;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.*;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class LobbyServer implements ClientEventListener {
    private static final Map<String, String> nickToPass = new ConcurrentHashMap<>(); // maps username to password
    private static final Map<UUID, WaitingLobby> waitingLobbyMap = new ConcurrentHashMap<>();
    private static final Map<UUID, GameLobby> gameLobbyMap = new ConcurrentHashMap<>();

    private final SocketWrapper sw;
    private ClientEventHandler eventHandler;
    private String nickname;
    private WaitingLobby currentLobby;
    private UUID currentLobbyID;
    private State state;

    private LobbyServer(SocketWrapper sw) {
        this.sw = sw;
        this.eventHandler = new ClientEventHandler(this);
        this.state = State.ACCEPT_PHASE;
    }

    public static void spawn(SocketWrapper socketWrapper) {
        LobbyServer lobbyServer = new LobbyServer(socketWrapper);
        SocketListener.subscribe(socketWrapper, lobbyServer.getEventHandler());
    }

    private ClientEventHandler getEventHandler() {
        return this.eventHandler;
    }

    @Override
    public synchronized void receive(ClientEvent event) {
        Logger log = Logger.getLogger(this.getClass().getName());
        log.info("Lobby server received a new Event: " + event.getClass());
        // if a client has disconnected reset the lobby
        if (event.getClass() == ClientDisconnect.class) {
            this.currentLobby.removePlayerHandler(this.nickname);
            this.eventHandler = null;
            log.info("Lobby server was closed for player: " +
                    nickname +
                    " on address " +
                    this.sw.getInetAddress());
            return;
        }

        try {
            switch (this.state) {
                case ACCEPT_PHASE -> acceptPhase(event);
                case REDIRECT_PHASE -> redirectPhase(event);
                case GAME_START_PHASE -> gameStartPhase(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptPhase(ClientEvent clientEvent) throws IOException {
        if (clientEvent instanceof DeclarePlayer castedEvent) {
            this.nickname = castedEvent.getNickname();
            String password = castedEvent.getPassword();
            if (nickToPass.get(this.nickname) != null && !nickToPass.get(this.nickname).equals(password)) {
                sw.sendMessage(new LobbyAccept(StatusCode.Fail));
            } else {
                nickToPass.put(this.nickname, password);
                sw.sendMessage(new LobbyAccept(StatusCode.Success));
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
            case CreateLobby castedEvent -> {
                if (castedEvent.getMaxPlayers() < 1 || castedEvent.getMaxPlayers() > 4) {
                    sw.sendMessage(LobbyRedirect.fail());
                    break;
                }
                this.currentLobby = new WaitingLobby(
                        castedEvent.isPublic(),
                        castedEvent.getMaxPlayers(),
                        nickname,
                        this.getEventHandler()
                );

                this.currentLobbyID = generateUUID();
                this.state = State.GAME_START_PHASE;
                sw.sendMessage(LobbyRedirect.success(this.currentLobbyID));
            }
            case ConnectLobby castedEvent -> {
                UUID id = castedEvent.getCode();
                if (!waitingLobbyMap.containsKey(id) || !waitingLobbyMap.get(id).addPlayer(nickname, this.getEventHandler())) {
                    sw.sendMessage(LobbyRedirect.fail());
                    break;
                }
                this.currentLobby = waitingLobbyMap.get(id);
                sw.sendMessage(LobbyRedirect.success(id));
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
            case StartGame castedEvent -> {
                GameLobby gameLobby;
                try {
                    gameLobby = this.currentLobby.getGameLobby(castedEvent.getGameMode());
                } catch (InputValidationException e) {
                    sw.sendMessage(GameInit.fail(e.getMessage()));
                    return;
                }
                // code executes only when a gameLobby was created
                waitingLobbyMap.remove(this.currentLobbyID);
                gameLobbyMap.put(this.currentLobbyID, gameLobby);
                sw.sendMessage(GameInit.success());
                gameLobby.notifyGameStarted();
            }
            case GameStarted castedEvent -> {
                // todo create a new GameServer and change listener on the handler to that
                // todo send message to client saying server has started
            }
            case default -> sw.sendMessage(new InvalidRequest());
        }
    }

    private static UUID generateUUID() {
        UUID id = UUID.randomUUID();
        while (waitingLobbyMap.containsKey(id) || gameLobbyMap.containsKey(id)) {
            id = UUID.randomUUID();
        }
        return id;
    }

    private enum State {
        ACCEPT_PHASE,
        REDIRECT_PHASE,
        GAME_START_PHASE,
    }
}