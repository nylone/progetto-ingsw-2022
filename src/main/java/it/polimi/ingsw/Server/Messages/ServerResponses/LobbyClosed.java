package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class LobbyClosed extends Response {
    @Serial
    private static final long serialVersionUID = 309L;

    public LobbyClosed() {
        super(StatusCode.Success);
    }

}
