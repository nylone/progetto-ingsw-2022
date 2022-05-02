package it.polimi.ingsw.Server.Messages.ServerMessages;

import it.polimi.ingsw.Server.Messages.Enums.ResponseType;
import it.polimi.ingsw.Server.Messages.Enums.StatusCode;

public class LobbyServerAccept extends Response {
    public LobbyServerAccept(StatusCode statusCode) {
        super(statusCode, ResponseType.LOBBY_SERVER_ACCEPT, null);
    }
}
