package it.polimi.ingsw.Server.Messages.ServerMessages;

import it.polimi.ingsw.Server.Messages.Enums.ResponseType;
import it.polimi.ingsw.Server.Messages.Enums.StatusCode;

public abstract class Response {
    private final StatusCode statusCode;
    private final ResponseType responseType;
    private final ResponseBody body;

    public Response(StatusCode statusCode, ResponseType responseType, ResponseBody body) {
        this.statusCode = statusCode;
        this.responseType = responseType;
        this.body = body;
    }
}
