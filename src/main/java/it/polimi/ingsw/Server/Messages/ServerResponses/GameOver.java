package it.polimi.ingsw.Server.Messages.ServerResponses;

import java.io.Serial;
import java.util.List;

public class GameOver extends FixedStatusResponse {
    @Serial
    private static final long serialVersionUID = 315L;
    private final List<String> winners;

    public GameOver(List<String> winners) {
        this.winners = winners;
    }

    public List<String> getWinners() {
        return this.winners;
    }
}
