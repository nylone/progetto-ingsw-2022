package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.RemoteView.Messages.ClientEvents.*;
import it.polimi.ingsw.RemoteView.Messages.ClientEvents.Event;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.LobbyServerRedirect;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.StatusCode;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.InvalidRequest;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.LobbyServerAccept;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

public class LobbyServer {
    private static final Map<String, String> nickToPass = new ConcurrentHashMap<>(); // maps username to password
    private static final Map<UUID, Lobby> lobbyMap = new ConcurrentHashMap<>();

    private final SocketWrapper sw;
    private final BlockingQueue<Event> eventQueue;
    private String nickname;

    public LobbyServer(SocketWrapper sw) {
        this.sw = sw;
        this.eventQueue = new SynchronousQueue<>();
        SocketListener.listenAndEcho(sw, this.eventQueue); // start a listener and link it to the channel
    }

    protected void asyncHandle() {
        new Thread(() -> {
            try {
                acceptPhase();
                redirectPhase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void acceptPhase() throws InterruptedException, IOException {
        for (boolean again = true; again; ) {
            Event event = eventQueue.take();
            if (event instanceof DeclarePlayer castedEvent) {
                this.nickname = castedEvent.getNickname();
                String password = castedEvent.getPassword();
                if (nickToPass.get(this.nickname) != null && !nickToPass.get(this.nickname).equals(password)) {
                    sw.sendMessage(new LobbyServerAccept(StatusCode.Fail));
                } else {
                    nickToPass.put(this.nickname, password);
                    sw.sendMessage(new LobbyServerAccept(StatusCode.Success));
                    again = false;
                }
            } else {
                sw.sendMessage(new InvalidRequest());
            }
        }
    }

    private void redirectPhase() throws InterruptedException, IOException {
        // redirect phase: wait for valid lobby action
        // either:
        // - create
        // - join
        // - join started game (todo)
        for (boolean again = true; again;) {
            Event event = eventQueue.take();
            switch (event) {
                case CreateLobby castedEvent -> {
                    if (castedEvent.getMaxPlayers() < 1 || castedEvent.getMaxPlayers() > 4) {
                        sw.sendMessage(new InvalidRequest());
                        break;
                    }
                    Lobby lobby = new Lobby(
                            castedEvent.isPublic(),
                            castedEvent.getMaxPlayers(),
                            nickname,
                            eventQueue
                    );
                    // get a new lobby id
                    // put if absent adds the mapping to the object only if it's not yet been mapped.
                    // in case the action is run positively the return value will be null.
                    // so we cycle until we get a null, generating new ids along the way.
                    UUID id = UUID.randomUUID();
                    for (; lobbyMap.putIfAbsent(id, lobby) != null; id = UUID.randomUUID());
                    sw.sendMessage(new LobbyServerRedirect(StatusCode.Success, id));
                    again = false;
                }
                case ConnectLobby castedEvent -> {
                    UUID id = castedEvent.getCode();
                    if (!lobbyMap.containsKey(id) || !lobbyMap.get(id).addPlayer(nickname, eventQueue)) {
                        sw.sendMessage(new InvalidRequest());
                        break;
                    }
                    sw.sendMessage(new LobbyServerRedirect(StatusCode.Success, id));
                    again = false;
                }
                case default -> sw.sendMessage(new InvalidRequest());
            }
        }
    }
}