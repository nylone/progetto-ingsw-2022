package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

/**
 * A {@link Response} to a {@link it.polimi.ingsw.Server.Messages.Events.Requests.StartGameRequest}
 */
public class GameInit extends Response {
    @Serial
    private static final long serialVersionUID = 305L;
    private final String errorMessage;

    /**
     * Construct the response
     *
     * @param statusCode   the status code of the response
     * @param errorMessage additional feedback about the response
     */
    private GameInit(StatusCode statusCode, String errorMessage) {
        super(statusCode);
        this.errorMessage = errorMessage;
    }

    /**
     * Returns a failed status code response
     *
     * @param errorMessage additional feedback about failure
     * @return a failed status code response
     */
    public static GameInit fail(String errorMessage) {
        return new GameInit(StatusCode.Fail, errorMessage);
    }

    /**
     * Returns a successful status code response
     *
     * @return a successful status code response
     */
    public static GameInit success() {
        return new GameInit(StatusCode.Success, null);
    }

    /**
     * Get information about the response
     *
     * @return additional feedback about the response
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
