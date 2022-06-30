package it.polimi.ingsw.Server.Messages.ServerResponses;

import java.io.Serial;

/**
 * This Response is generated when a lobby's game is started and is sent to the lobby's clients.
 * Therefore, it is {@link FixedStatusResponse}
 */
public class GameStarted extends FixedStatusResponse {
    @Serial
    private static final long serialVersionUID = 306L;
}
