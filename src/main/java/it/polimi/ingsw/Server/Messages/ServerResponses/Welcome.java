package it.polimi.ingsw.Server.Messages.ServerResponses;

import java.io.Serial;

/**
 * A {@link Response} to represent the start of a connection with a {@link it.polimi.ingsw.Server.WelcomeServer}.
 * {@link #getStatusCode()} will always return positively
 */
public class Welcome extends FixedStatusResponse {
    @Serial
    private static final long serialVersionUID = 302L;

    /**
     * Construct the response
     */
    public Welcome() {
        super();
    }

}
