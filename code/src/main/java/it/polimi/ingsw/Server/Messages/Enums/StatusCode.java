package it.polimi.ingsw.Server.Messages.Enums;

/**
 * StatusCode represents the a status.
 * A status can be:
 * {@link #Query}<br>
 * {@link #Success}<br>
 * {@link #Fail}
 */
public enum StatusCode {
    /**
     * Status: waiting for an answer
     */
    Query,
    /**
     * Status: positive outcome
     */
    Success,
    /**
     * Status: negative outcome
     */
    Fail
}
