package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class Welcome extends Response {
    public Welcome(StatusCode statusCode) {
        super(statusCode);
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_WELCOME;
    }
}
