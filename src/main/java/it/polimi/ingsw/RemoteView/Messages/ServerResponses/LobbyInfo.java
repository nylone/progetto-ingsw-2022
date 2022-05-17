package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Lobby;

import java.util.List;
import java.util.UUID;

public class LobbyInfo {
    private final String admin;
    private final UUID ID;
    private final boolean isPublic;
    private final int maxPlayers;
    private final List<String> players;
    private final List<String> disconnectedPlayers;

    public LobbyInfo(Lobby lobby) {
        this.admin = lobby.getAdmin();
        this.ID = lobby.getId();
        this.isPublic = lobby.isPublic();
        this.maxPlayers = lobby.getMaxPlayers();
        this.players = lobby.getPlayers();
        this.disconnectedPlayers = lobby.getDisconnectedPlayers();
    }

    public String getAdmin() {
        return admin;
    }

    public UUID getID() {
        return ID;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public List<String> getPlayers() {
        return players;
    }

    public List<String> getDisconnectedPlayers() {
        return disconnectedPlayers;
    }
}