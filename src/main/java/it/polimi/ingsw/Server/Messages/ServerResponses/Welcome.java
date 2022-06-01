package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class Welcome extends Response {
    @Serial
    private static final long serialVersionUID = 302L;

    public Welcome(StatusCode statusCode) {
        super(statusCode);
    }

}
