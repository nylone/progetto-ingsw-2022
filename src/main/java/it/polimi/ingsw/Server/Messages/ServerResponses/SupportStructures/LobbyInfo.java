package it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures;

import it.polimi.ingsw.Server.Lobby;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Information about a {@link Lobby} to be shared with clients
 */
public class LobbyInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 314L;
    private final String admin;
    private final UUID ID;
    private final boolean isPublic;
    private final int maxPlayers;
    private final List<String> players;

    /**
     * Generate the lobby info
     * @param lobby the lobby to get the info of
     */
    public LobbyInfo(Lobby lobby) {
        this.admin = lobby.getAdmin();
        this.ID = lobby.getId();
        this.isPublic = lobby.isPublic();
        this.maxPlayers = lobby.getMaxPlayers();
        this.players = lobby.getPlayers();
    }

    /**
     * Get the admin nickname
     * @return the admin nickname
     */
    public String getAdmin() {
        return admin;
    }

    /**
     * Get the UUID of the Lobby
     * @return the UUID of the Lobby
     */
    public UUID getID() {
        return ID;
    }

    /**
     * Check if the Lobby is public
     * @return true if the lobby is public
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Get the maximum amount of players allowed in the lobby
     * @return the max size of the lobby
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Get connected player nicknames
     * @return an Unmodifiable {@link List} containing the connected players
     */
    public List<String> getPlayers() {
        return List.copyOf(players);
    }
}
