package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class GameInit extends Response {
    @Serial
    private static final long serialVersionUID = 305L;
    private final String errorMessage;

    private GameInit(StatusCode statusCode, String errorMessage) {
        super(statusCode);
        this.errorMessage = errorMessage;
    }

    public static GameInit fail(String errorMessage) {
        return new GameInit(StatusCode.Fail, errorMessage);
    }

    public static GameInit success() {
        return new GameInit(StatusCode.Success, null);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
