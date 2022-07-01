package it.polimi.ingsw.Server.Messages.ServerResponses;

import java.io.Serial;
import java.util.List;

/**
 * This Response is generated when a lobby's game is over and is sent to the lobby's clients.
 * Therefore, it is {@link FixedStatusResponse}
 */
public class GameOver extends FixedStatusResponse {
    @Serial
    private static final long serialVersionUID = 315L;
    private final List<String> winners;

    /**
     * Create the response
     *
     * @param winners the winners of the game
     */
    public GameOver(List<String> winners) {
        this.winners = winners;
    }

    /**
     * Get the winners of the game
     *
     * @return an Unmodifiable {@link List} containing the list of winners for the game
     */
    public List<String> getWinners() {
        return List.copyOf(this.winners);
    }
}
