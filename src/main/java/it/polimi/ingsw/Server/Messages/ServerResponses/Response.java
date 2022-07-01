package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.Message;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

/**
 * Any message sent by the {@link it.polimi.ingsw.Server.WelcomeServer} or {@link it.polimi.ingsw.Server.LobbyServer} will
 * inherit this class.
 */
public abstract class Response extends Message {
    @Serial
    private static final long serialVersionUID = 301L;
    private final StatusCode statusCode;

    /**
     * Construct the response
     *
     * @param statusCode the status code of the response
     */
    public Response(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Get the status code of the response
     *
     * @return true if the response is positive, false otherwise
     */
    public StatusCode getStatusCode() {
        return statusCode;
    }
}
