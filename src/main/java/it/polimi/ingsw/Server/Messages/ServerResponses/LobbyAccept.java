package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.LobbyInfo;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;
import java.util.List;

/**
 * A {@link Response} to represent the output the controller gave on a previous
 * {@link it.polimi.ingsw.Server.Messages.Events.Requests.DeclarePlayerRequest}
 */
public class LobbyAccept extends Response {
    @Serial
    private static final long serialVersionUID = 308L;

    private final List<LobbyInfo> publicLobbies;

    /**
     * Construct the response
     *
     * @param statusCode  the status code of the response
     * @param openLobbies a {@link List} of open lobbies the client may join or null if the response is negative
     */
    private LobbyAccept(StatusCode statusCode, List<LobbyInfo> openLobbies) {
        super(statusCode);
        this.publicLobbies = openLobbies;
    }

    /**
     * Returns a failed status code response
     *
     * @return a failed status code response
     */
    public static LobbyAccept fail() {
        return new LobbyAccept(StatusCode.Fail, null);
    }

    /**
     * Returns a successful status code response
     *
     * @param openLobbies a {@link List} of open lobbies the client may join
     * @return a successful status code response
     */
    public static LobbyAccept success(List<LobbyInfo> openLobbies) {
        return new LobbyAccept(StatusCode.Success, openLobbies);
    }

    /**
     * Get a list of the open lobbies the client may join
     *
     * @return a {@link List} of open lobbies the client may join or null if {@link #getStatusCode()} is {@link StatusCode#Fail}
     */
    public List<LobbyInfo> getPublicLobbies() {
        return publicLobbies;
    }

}
