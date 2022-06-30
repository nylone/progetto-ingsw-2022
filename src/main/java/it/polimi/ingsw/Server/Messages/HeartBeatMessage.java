package it.polimi.ingsw.Server.Messages;

import it.polimi.ingsw.Server.Messages.Events.Requests.ClientRequest;

import java.io.Serial;

/**
 * A message used to keep the connection alive, used in {@link it.polimi.ingsw.Network.KeepAliveSocketWrapper}
 */
public class HeartBeatMessage extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 357L;
}
