package it.polimi.ingsw.Server.Messages.Events.Requests;

import java.io.Serial;

/**
 * Represents a Client triying to set its nickname after a successful connection to the server.
 */
public class DeclarePlayerRequest extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 354L;
    private final String nickname;

    /**
     * Construct the request
     * @param nickname the nickname the user wishes to set
     */
    public DeclarePlayerRequest(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Get the nickname the user selected
     * @return the nickname of the user
     */
    public String getNickname() {
        return nickname;
    }
}
