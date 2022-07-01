package it.polimi.ingsw.Server.Messages.Events.Requests;

import java.io.Serial;
import java.util.UUID;

/**
 * Represents a Client triying to connect to a {@link it.polimi.ingsw.Server.Lobby}
 */
public class ConnectLobbyRequest extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 352L;

    private final UUID code;

    /**
     * Create the request to connect to a lobby.
     *
     * @param code the {@link UUID} of the lobby the client wishes to connect to.
     */
    public ConnectLobbyRequest(UUID code) {
        this.code = code;
    }

    /**
     * Get the code of the lobby
     *
     * @return the {@link it.polimi.ingsw.Server.Lobby}'s {@link UUID} the client wishes to connect to
     */
    public UUID getCode() {
        return code;
    }
}
