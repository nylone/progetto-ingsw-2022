package it.polimi.ingsw.RemoteView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Lobby {
    private final String admin;
    private final boolean isPublic;
    private final int maxPlayers;
    private final List<String> waitingPlayers;
    private final Map<String, ClientEventHandler> playerEventSources;

    public Lobby(boolean isPublic, int maxPlayers, String admin, ClientEventHandler adminChannel) {
        this.admin = admin;
        this.isPublic = isPublic;
        this.maxPlayers = maxPlayers;
        this.waitingPlayers = new ArrayList<>();
        this.waitingPlayers.add(admin);
        this.playerEventSources = new ConcurrentHashMap<>();
        this.playerEventSources.put(admin, adminChannel);
    }

    public String getAdmin() {
        return admin;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean addPlayer(String nick, ClientEventHandler playerChannel) {
        synchronized (this.waitingPlayers) {
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

    public ClientEventHandler removePlayerHandler(String nick) {
        synchronized (this.waitingPlayers) {
            return this.playerEventSources.remove(nick);
        }
    }

}
