package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class InvalidRequest extends Response {
    @Serial
    private static final long serialVersionUID = 307L;

    public InvalidRequest() {
        super(StatusCode.Fail);
    }

}
