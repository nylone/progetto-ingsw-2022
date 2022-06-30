package it.polimi.ingsw.Server.Messages.ServerResponses;

import java.io.Serial;

/**
 * This Response is generated when a lobby is closed and is sent to the lobby's clients. Therefore, it is {@link FixedStatusResponse}
 */
public class LobbyClosed extends FixedStatusResponse {
    @Serial
    private static final long serialVersionUID = 309L;
}
