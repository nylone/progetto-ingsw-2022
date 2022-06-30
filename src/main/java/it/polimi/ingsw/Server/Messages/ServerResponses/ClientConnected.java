package it.polimi.ingsw.Server.Messages.ServerResponses;

import java.io.Serial;
import java.util.List;

/**
 * This Response is generated when a lobby's client connects and is sent to the lobby's clients.
 * Therefore, it is {@link FixedStatusResponse}
 */
public class ClientConnected extends FixedStatusResponse {
    @Serial
    private static final long serialVersionUID = 303L;
    private final String lastConnectedNickname;

    private final List<String> players;

    /**
     * Create the response
     * @param lastConnectedNickname the nickname of the player that just connected
     * @param players the list of all connected players
     */
    public ClientConnected(String lastConnectedNickname, List<String> players) {
        this.lastConnectedNickname = lastConnectedNickname;
        this.players = players;
    }

    /**
     * Get the user that generated this response by connecting
     * @return the nickname of the user that just connected to the lobby
     */
    public String getLastConnectedNickname() {
        return lastConnectedNickname;
    }

    /**
     * Get the players connected to the lobby
     * @return an Unmodifiable {@link List} containing all players in the lobby
     */
    public List<String> getPlayers() {
        return this.players;
    }
}
