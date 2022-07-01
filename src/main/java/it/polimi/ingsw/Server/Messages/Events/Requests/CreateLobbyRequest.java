package it.polimi.ingsw.Server.Messages.Events.Requests;

import java.io.Serial;

/**
 * Represents a Client triying to create a new {@link it.polimi.ingsw.Server.Lobby}
 */
public class CreateLobbyRequest extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 353L;
    private final boolean isPublic;
    private final int maxPlayers;

    /**
     * Construct the request
     *
     * @param isPublic   true if the lobby is supposed to be publicly available on the Server
     * @param maxPlayers the number of maximum players the lobby will allow (no bound is set but the server will impose it)
     */
    public CreateLobbyRequest(boolean isPublic, int maxPlayers) {
        this.isPublic = isPublic;
        this.maxPlayers = maxPlayers;
    }

    /**
     * Check if the lobby will be public
     *
     * @return true if the lobby will be public, false otherwise
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Get the maximum amount of players the lobby will have
     *
     * @return the maximum amount of players the lobby will have
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

}
