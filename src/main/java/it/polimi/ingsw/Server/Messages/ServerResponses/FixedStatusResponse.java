package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

/**
 * Represents a {@link Response} that is always sent with the same {@link StatusCode#Success}
 */
public abstract class FixedStatusResponse extends Response {
    @Serial
    private static final long serialVersionUID = 316L;

    /**
     * Constructs a fixed status response
     */
    public FixedStatusResponse() {
        super(StatusCode.Success);
    }
}
