package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class LobbyAccept extends Response {
    public LobbyAccept(StatusCode statusCode) {
        super(statusCode);
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_LOBBY_ACCEPT;
    }
}
