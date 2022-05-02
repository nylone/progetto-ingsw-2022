package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

/**
 * StatusCode represents the a status.
 * A status can be:
 * {@link #Success}<br>
 * {@link #Fail}
 */
public enum StatusCode {
    /**
     * Status: positive outcome
     */
    Success,
    /**
     * Status: negative outcome
     */
    Fail
}
