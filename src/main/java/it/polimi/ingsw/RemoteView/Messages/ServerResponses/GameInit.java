package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class GameInit extends Response {
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

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_GAME_INIT;
    }
}
