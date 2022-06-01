package it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures;

import java.io.Serial;
import java.io.Serializable;

/**
 * StatusCode represents the a status.
 * A status can be:
 * {@link #Success}<br>
 * {@link #Fail}
 */
public enum StatusCode implements Serializable {
    /**
     * Status: positive outcome
     */
    Success,
    /**
     * Status: negative outcome
     */
    Fail;
    @Serial
    private static final long serialVersionUID = 313L;
}
