package it.polimi.ingsw.Server.Messages.ServerMessages;

import it.polimi.ingsw.Server.Messages.Enums.ResponseType;
import it.polimi.ingsw.Server.Messages.Enums.StatusCode;

public class WelcomeServerAccept extends Response {
    public WelcomeServerAccept(StatusCode statusCode) {
        super(statusCode, ResponseType.WELCOME_SERVER_ACCEPT, null);
    }
}
