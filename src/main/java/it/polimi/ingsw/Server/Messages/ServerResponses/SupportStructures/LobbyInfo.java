package it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures;

import it.polimi.ingsw.Server.Lobby;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class LobbyInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 314L;
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
