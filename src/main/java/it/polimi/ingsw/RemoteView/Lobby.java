package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.Controller.GameHandler;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;
import it.polimi.ingsw.RemoteView.Messages.Events.Internal.ClientConnectEvent;
import it.polimi.ingsw.RemoteView.Messages.Events.Internal.ClientDisconnectEvent;
import it.polimi.ingsw.RemoteView.Messages.Events.Internal.GameStartEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Lobby {
    private final String admin;
    private final boolean isPublic;
    private final int maxPlayers;
    private final List<String> players;
    private final Map<String, ClientEventHandler> playerEventSources;
    private GameHandler gameHandler;

    protected Lobby(boolean isPublic, int maxPlayers, String admin, ClientEventHandler adminChannel) {
        this.admin = admin;
        this.isPublic = isPublic;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.players.add(admin);
        this.playerEventSources = new ConcurrentHashMap<>();
        this.playerEventSources.put(admin, adminChannel);
    }

    protected String getAdmin() {
        return admin;
    }

    protected GameHandler getGameHandler() {
        return this.gameHandler;
    }

    protected boolean isPublic() {
        return isPublic;
    }

    protected boolean isLobbyFull() {
        return this.players.size() == maxPlayers;
    }


    protected boolean addPlayer(String nick, ClientEventHandler playerChannel) {
        synchronized (this.players) {
            // in case of reconnection
            if (this.players.contains(nick)) {
                // prevents player hijacking
                if (!this.playerEventSources.containsKey(nick)) {
                    notifyPlayers(new ClientConnectEvent(nick, players.size()));
                    this.playerEventSources.put(nick, playerChannel);
                    return true;
                } else {
                    return false;
                }
                // in case of new connection check for max players and check that the game has not yet started
            } else if (this.gameHandler == null && this.maxPlayers > this.players.size()) {
                this.players.add(nick);
                notifyPlayers(new ClientConnectEvent(nick, players.size()));
                this.playerEventSources.put(nick, playerChannel);
                return true;
            } else {
                return false;
            }
        }
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

    protected void removePlayerHandler(String nick) {
        synchronized (this.players) {
            this.playerEventSources.remove(nick);
            notifyPlayers(new ClientDisconnectEvent(nick));
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