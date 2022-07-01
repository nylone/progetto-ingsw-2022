package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;
import java.util.UUID;

/**
 * A {@link Response} to represent the output the controller gave on a previous {@link it.polimi.ingsw.Server.Messages.Events.Requests.ConnectLobbyRequest} or
 * {@link it.polimi.ingsw.Server.Messages.Events.Requests.CreateLobbyRequest}
 */
public class LobbyConnected extends Response {
    @Serial
    private static final long serialVersionUID = 310L;
    private final UUID lobbyID;

    private final String admin;

    /**
     * Construct the response
     *
     * @param statusCode the status code of the response
     * @param lobbyID    the UUID of the lobby
     * @param admin      the admin of the lobby
     */
    private LobbyConnected(StatusCode statusCode, UUID lobbyID, String admin) {
        super(statusCode);
        this.lobbyID = lobbyID;
        this.admin = admin;
    }

    /**
     * Returns a failed status code response
     *
     * @return a failed status code response
     */
    public static LobbyConnected fail() {
        return new LobbyConnected(StatusCode.Fail, null, null);
    }

    /**
     * Returns a successful status code response
     *
     * @param lobbyID the UUID of the successfully connected to lobby
     * @param admin   the admin of the lobby
     * @return a successful status code response
     */
    public static LobbyConnected success(UUID lobbyID, String admin) {
        return new LobbyConnected(StatusCode.Success, lobbyID, admin);
    }

    /**
     * Get the id of the lobby
     *
     * @return the UUID of the lobby or null if no lobby was linked to the response
     */
    public UUID getLobbyID() {
        return this.lobbyID;
    }

    /**
     * Get the admin of the lobby
     *
     * @return the nickname of the admin of the lobby or null if no lobby was linked to the response
     */
    public String getAdmin() {
        return admin;
    }

}
