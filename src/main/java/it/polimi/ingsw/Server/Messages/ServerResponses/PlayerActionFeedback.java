package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

/**
 * A {@link Response} to represent the output the controller gave on a previous {@link it.polimi.ingsw.Server.Messages.Events.Requests.PlayerActionRequest}
 */
public class PlayerActionFeedback extends Response {
    @Serial
    private static final long serialVersionUID = 312L;

    private final String report;

    /**
     * Construct the response
     * @param statusCode the status code of the response
     * @param report     additional feedback about the response
     */
    private PlayerActionFeedback(StatusCode statusCode, String report) {
        super(statusCode);
        this.report = report;
    }

    /**
     * Returns a failed status code response
     * @param report additional feedback about failure
     * @return a failed status code response
     */
    public static PlayerActionFeedback fail(String report) {
        return new PlayerActionFeedback(StatusCode.Fail, report);
    }

    /**
     * Returns a successful status code response
     * @return a successful status code response
     */
    public static PlayerActionFeedback success() {
        return new PlayerActionFeedback(StatusCode.Success, "Action executed successfully");
    }

    /**
     * Get information about the response
     * @return additional feedback about the response
     */
    public String getReport() {
        return report;
    }
}
