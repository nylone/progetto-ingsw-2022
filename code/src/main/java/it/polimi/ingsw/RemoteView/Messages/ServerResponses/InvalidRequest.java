package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class InvalidRequest extends Response {
    public InvalidRequest() {
        super(StatusCode.Fail);
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_INVALID_REQUEST;
    }
}
