package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class Welcome extends Response {
    @Serial
    private static final long serialVersionUID = 302L;

    public Welcome(StatusCode statusCode) {
        super(statusCode);
    }

}
