package it.polimi.ingsw.Server.Messages.Events.Requests;

import it.polimi.ingsw.Server.Messages.Events.ClientEvent;
import it.polimi.ingsw.Server.Messages.Message;

import java.io.Serial;

public abstract class ClientRequest extends Message implements ClientEvent {
    @Serial
    private static final long serialVersionUID = 351L;
}
