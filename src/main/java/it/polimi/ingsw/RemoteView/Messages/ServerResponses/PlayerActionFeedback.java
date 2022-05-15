package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class PlayerActionFeedback extends Response {
    private final String report;

    private PlayerActionFeedback(StatusCode statusCode, String report) {
        super(statusCode);
        this.report = report;
    }

    public static PlayerActionFeedback fail(String report) {
        return new PlayerActionFeedback(StatusCode.Fail, report);
    }

    public static PlayerActionFeedback success() {
        return new PlayerActionFeedback(StatusCode.Success, "Action executed successfully");
    }

    public String getReport() {
        return report;
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_PLAYER_ACTION;
    }
}
