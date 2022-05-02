package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class LobbyServerAccept extends Response {
    public LobbyServerAccept(StatusCode statusCode) {
        super(statusCode);
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_LOBBY_SERVER_ACCEPT;
    }
}
