package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.Controller.GameHandler;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;
import it.polimi.ingsw.RemoteView.Messages.Events.Internal.LobbyClosedEvent;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.ClientRequest;
import it.polimi.ingsw.RemoteView.Messages.Events.Internal.ClientConnectEvent;
import it.polimi.ingsw.RemoteView.Messages.Events.Internal.ClientDisconnectEvent;
import it.polimi.ingsw.RemoteView.Messages.Events.Internal.GameStartEvent;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.LobbyClosed;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static it.polimi.ingsw.RemoteView.LobbyServer.lobbyMap;

public class Lobby {
    private final UUID id;
    private final String admin;
    private final boolean isPublic;
    private boolean isClosed;
    private final int maxPlayers;
    private final List<String> players;
    private final Map<String, ClientEventHandler> playerEventSources;
    private GameHandler gameHandler;

    protected Lobby(UUID id, boolean isPublic, int maxPlayers, String admin) {
        this.id = id;
        this.admin = admin;
        this.isPublic = isPublic;
        this.isClosed = false;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.playerEventSources = new ConcurrentHashMap<>();
    }

    public UUID getId() {
        return id;
    }

    public String getAdmin() {
        return admin;
    }

    public GameHandler getGameHandler() {
        return this.gameHandler;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isLobbyFull() {
        return this.players.size() == maxPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public List<String> getPlayers() {
        synchronized (this.players) {
            return List.copyOf(this.players);
        }
    }

    public List<String> getDisconnectedPlayers() {
        synchronized (this.players) {
            Set<String> connected = this.playerEventSources.keySet();
            return this.players.stream()
                    .filter(Predicate.not(connected::contains))
                    .toList();
        }
    }

    public boolean isWaitingLobby() {
        return this.gameHandler == null;
    }

    public void notifyPlayers(ClientEvent event) {
        synchronized (this.players) {
            for (ClientEventHandler ceh : this.playerEventSources.values()) {
                try {
                    ceh.enqueue(event);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean addPlayer(String nick, ClientEventHandler playerChannel) {
        synchronized (this.players) {
            if (this.isClosed) {
                return false;
            }
            // in case of reconnection
            if (this.players.contains(nick)) {
                // prevents player hijacking
                if (!this.playerEventSources.containsKey(nick)) {
                    this.playerEventSources.put(nick, playerChannel);
                    notifyPlayers(new ClientConnectEvent(nick, List.copyOf(this.players)));
                    return true;
                } else {
                    return false;
                }
                // in case of new connection check for max players and check that the game has not yet started
            } else if (this.gameHandler == null && this.maxPlayers > this.players.size()) {
                this.players.add(nick);
                this.playerEventSources.put(nick, playerChannel);
                notifyPlayers(new ClientConnectEvent(nick, List.copyOf(this.players)));
                return true;
            } else {
                return false;
            }
        }
    }

    protected void disconnectPlayer(String nick) {
        synchronized (this.players) {
            this.playerEventSources.remove(nick);
            this.players.remove(nick);
            notifyPlayers(new ClientDisconnectEvent(nick, List.copyOf(this.players)));
            if (!this.isWaitingLobby() || this.admin.equals(nick)) {
                lobbyMap.remove(this.id);
                notifyPlayers(new LobbyClosedEvent());
                this.players.clear();
                this.playerEventSources.clear();
                this.isClosed = true;
            }
        }
    }

    protected void startGame(GameMode gameMode) throws InputValidationException {
        synchronized (this.players) {
            notifyPlayers(new GameStartEvent());
            this.gameHandler = new GameHandler(gameMode, this.players.toArray(new String[0]));
            this.gameHandler.subscribeLobby(this);
        }
    }

}
