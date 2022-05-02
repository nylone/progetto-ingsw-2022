package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.RemoteView.Messages.ClientEvents.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Lobby {
    private final String admin;
    private final boolean isPublic;
    private final byte maxPlayers;
    private final List<String> waitingPlayers;
    private final Map<String, BlockingQueue<Event>> playerChannels;

    public Lobby(boolean isPublic, byte maxPlayers,String admin, BlockingQueue<Event> adminChannel) {
        this.admin = admin;
        this.isPublic = isPublic;
        this.maxPlayers = maxPlayers;
        this.waitingPlayers = new ArrayList<>();
        this.waitingPlayers.add(admin);
        this.playerChannels = new ConcurrentHashMap<>();
        this.playerChannels.put(admin, adminChannel);
    }

    public String getAdmin() {
        return admin;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean addPlayer(String nick, BlockingQueue<Event> playerChannel) {
        synchronized (this.waitingPlayers) {
            if (this.maxPlayers > this.waitingPlayers.size()) {
                this.waitingPlayers.add(nick);
                this.playerChannels.put(nick, playerChannel);
                return true;
            } else return false;
        }
    }
}
