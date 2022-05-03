package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.RemoteView.Messages.ClientEvents.*;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.InvalidRequest;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.LobbyServerAccept;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.LobbyServerRedirect;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.StatusCode;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyServer implements ClientEventListener {
    private static final Map<String, String> nickToPass = new ConcurrentHashMap<>(); // maps username to password
    private static final Map<UUID, Lobby> lobbyMap = new ConcurrentHashMap<>();

    private final SocketWrapper sw;
    private ClientEventHandler eventHandler;
    private String nickname;
    private Lobby currentLobby;
    private State state;

    private LobbyServer(SocketWrapper sw) {
        this.sw = sw;
        this.eventHandler = new ClientEventHandler(this);
        this.state = State.Accepting;
    }

    public static void spawn(SocketWrapper socketWrapper) {
        LobbyServer lobbyServer = new LobbyServer(socketWrapper);
        SocketListener.subscribe(socketWrapper, lobbyServer.getEventHandler());
    }

    @Override
    public synchronized void receive(ClientEvent event) {
        // if a client has disconnected reset the lobby
        if (event.getClass() == ClientDisconnect.class) {
            this.currentLobby.removePlayerHandler(this.nickname);
            this.eventHandler = null;
            System.out.println("Lobby server was closed for player: " + nickname);
        }

        try {
            switch (this.state) {
                case Accepting -> acceptPhase(event);
                case Redirecting -> redirectPhase(event);
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
                sw.sendMessage(new LobbyServerAccept(StatusCode.Fail));
            } else {
                nickToPass.put(this.nickname, password);
                sw.sendMessage(new LobbyServerAccept(StatusCode.Success));
                this.state = State.Redirecting;
            }
        } else {
                sw.sendMessage(new InvalidRequest());
            }
    }

    private void redirectPhase(ClientEvent clientEvent) throws IOException {
        // redirect phase: wait for valid lobby action
        // either:
        // - create
        // - join
        // - join started game (todo)
        switch (clientEvent) {
            case CreateLobby castedEvent -> {
                if (castedEvent.getMaxPlayers() < 1 || castedEvent.getMaxPlayers() > 4) {
                    sw.sendMessage(LobbyServerRedirect.fail());
                    break;
                }
                this.currentLobby = new Lobby(
                        castedEvent.isPublic(),
                        castedEvent.getMaxPlayers(),
                        nickname,
                        this.getEventHandler()
                );
                // get a new lobby id
                // put if absent adds the mapping to the object only if it's not yet been mapped.
                // in case the action is run positively the return value will be null.
                // so we cycle until we get a null, generating new ids along the way.
                UUID id = UUID.randomUUID();
                for (; lobbyMap.putIfAbsent(id, this.currentLobby) != null; id = UUID.randomUUID()) ;
                sw.sendMessage(LobbyServerRedirect.success(id));
                this.state = State.Waiting;
            }
                case ConnectLobby castedEvent -> {
                    UUID id = castedEvent.getCode();
                    if (!lobbyMap.containsKey(id) || !lobbyMap.get(id).addPlayer(nickname, this.getEventHandler())) {
                        sw.sendMessage(LobbyServerRedirect.fail());
                        break;
                    }
                    this.currentLobby = lobbyMap.get(id);
                    sw.sendMessage(LobbyServerRedirect.success(id));
                    this.state = State.Waiting;
                }
            case default -> sw.sendMessage(new InvalidRequest());
        }
    }

    private ClientEventHandler getEventHandler() {
        return this.eventHandler;
    }

    private enum State {
        Accepting,
        Redirecting,
        Waiting,
    }
}