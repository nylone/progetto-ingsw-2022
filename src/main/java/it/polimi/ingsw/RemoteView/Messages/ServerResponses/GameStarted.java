package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class GameStarted extends Response {
    @Serial
    private static final long serialVersionUID = 306L;

    public GameStarted() {
        super(StatusCode.Success);
    }

}
