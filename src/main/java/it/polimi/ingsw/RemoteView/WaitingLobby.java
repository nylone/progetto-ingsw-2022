package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.RemoteView.Messages.ClientEvents.GameStarted;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WaitingLobby {
    private final String admin;
    private final boolean isPublic;
    private final int maxPlayers;
    private final List<String> waitingPlayers;
    private final Map<String, ClientEventHandler> playerEventSources;
    private boolean lobbyLocked = false;

    protected WaitingLobby(boolean isPublic, int maxPlayers, String admin, ClientEventHandler adminChannel) {
        this.admin = admin;
        this.isPublic = isPublic;
        this.maxPlayers = maxPlayers;
        this.waitingPlayers = new ArrayList<>();
        this.waitingPlayers.add(admin);
        this.playerEventSources = new ConcurrentHashMap<>();
        this.playerEventSources.put(admin, adminChannel);
    }

    protected String getAdmin() {
        return admin;
    }

    protected boolean isPublic() {
        return isPublic;
    }

    protected boolean addPlayer(String nick, ClientEventHandler playerChannel) {
        synchronized (this.waitingPlayers) {
            // the event may have been processed too late and the waiting lobby may now be closed
            if (this.lobbyLocked) {
                return false;
            }
            // in case of reconnection
            if (this.waitingPlayers.contains(nick)) {
                if (!this.playerEventSources.containsKey(nick)) {
                    this.playerEventSources.put(nick, playerChannel);
                    return true;
                } else {
                    return false;
                }
            } else if (this.maxPlayers > this.waitingPlayers.size()) {
                this.waitingPlayers.add(nick);
                this.playerEventSources.put(nick, playerChannel);
                return true;
            } else {
                return false;
            }
        }
    }

    protected void removePlayerHandler(String nick) {
        synchronized (this.waitingPlayers) {
            this.playerEventSources.remove(nick);
        }
    }

    protected GameLobby getGameLobby(GameMode gameMode) throws InputValidationException {
        synchronized (this.waitingPlayers) {
            this.lobbyLocked = true;
            return new GameLobby(gameMode, this.waitingPlayers, this.playerEventSources);
        }
    }

}
