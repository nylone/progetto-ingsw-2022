package it.polimi.ingsw.Server.Messages;

import it.polimi.ingsw.Server.Messages.Events.Requests.ClientRequest;

import java.io.Serial;

public class HeartBeatMessage extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 357L;
}
