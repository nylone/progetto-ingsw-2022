package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public abstract class FixedStatusResponse extends Response {
    @Serial
    private static final long serialVersionUID = 316L;

    public FixedStatusResponse() {
        super(StatusCode.Success);
    }
}
