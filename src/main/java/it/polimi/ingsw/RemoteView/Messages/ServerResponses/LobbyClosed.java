package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class LobbyClosed extends Response {
    @Serial
    private static final long serialVersionUID = 309L;

    public LobbyClosed() {
        super(StatusCode.Success);
    }

}
