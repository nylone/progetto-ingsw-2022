package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

/**
 * A {@link Response} to represent the start of a connection with a {@link it.polimi.ingsw.Server.WelcomeServer}
 */
public class Welcome extends Response {
    @Serial
    private static final long serialVersionUID = 302L;

    /**
     * Construct the response
     * @param statusCode the status code of the response
     */
    public Welcome(StatusCode statusCode) {
        super(statusCode);
    }

}
