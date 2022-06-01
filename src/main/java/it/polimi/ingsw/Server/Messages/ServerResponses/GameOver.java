package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;
import java.util.List;

public class GameOver extends Response {
    @Serial
    private static final long serialVersionUID = 315L;
    private final List<String> winners;

    public GameOver(List<String> winners) {
        super(StatusCode.Success);
        this.winners = winners;
    }

    public List<String> getWinners() {
        return this.winners;
    }
}
