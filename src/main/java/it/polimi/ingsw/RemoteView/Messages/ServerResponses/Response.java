package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

public abstract class Response implements MessageBuilder {
    private final StatusCode statusCode;

    public Response(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }
}
