package it.polimi.ingsw.Server.Messages;

import it.polimi.ingsw.Server.Messages.Enums.StatusCode;

public interface Message {
    /**
     * Get the StatusCode associated to the message: <br>
     * - messages coming from clients should always return {@link StatusCode#Query}; <br>
     * - messages coming from servers should always return either {@link StatusCode#Success} or {@link StatusCode#Fail}. <br>
     * @return a StatusCode object. <br>
     */
    StatusCode getStatusCode();

    /**
     * Get a human readable output associated with the message
     * @return a human readable String.
     */
    String getHumanMeaning();
}
