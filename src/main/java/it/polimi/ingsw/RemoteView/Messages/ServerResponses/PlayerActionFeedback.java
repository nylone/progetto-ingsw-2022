package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class PlayerActionFeedback extends Response {
    @Serial
    private static final long serialVersionUID = 312L;

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
}
