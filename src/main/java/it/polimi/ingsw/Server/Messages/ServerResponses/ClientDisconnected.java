package it.polimi.ingsw.Server.Messages.ServerResponses;

import java.io.Serial;
import java.util.List;

/**
 * This Response is generated when a lobby's client disconnected and is sent to the lobby's clients.
 * Therefore, it is {@link FixedStatusResponse}
 */
public class ClientDisconnected extends FixedStatusResponse {

    @Serial
    private static final long serialVersionUID = 304L;
    private final String lastDisconnectedNickname;

    private final List<String> players;

    /**
     * Create the response
     * @param lastDisconnectedNickname the nickname of the player that just disconnected
     * @param players the list of still connected players
     */
    public ClientDisconnected(String lastDisconnectedNickname, List<String> players) {
        this.lastDisconnectedNickname = lastDisconnectedNickname;
        this.players = players;
    }

    /**
     * Get the players still connected to the lobby
     * @return an Unmodifiable {@link List} containing players still in the lobby
     */
    public List<String> getPlayers() {
        return List.copyOf(this.players);
    }

    /**
     * Get the user that generated this response by disconnecting
     * @return the nickname of the user that just disconnected from the lobby
     */
    public String getLastDisconnectedNickname() {
        return this.lastDisconnectedNickname;
    }

}
