package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class WelcomeServerAccept extends Response {
    public WelcomeServerAccept(StatusCode statusCode) {
        super(statusCode);
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_WELCOME_SERVER_ACCEPT;
    }
}
