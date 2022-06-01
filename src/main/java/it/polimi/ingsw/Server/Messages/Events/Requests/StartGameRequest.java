package it.polimi.ingsw.Server.Messages.Events.Requests;

import it.polimi.ingsw.Model.Enums.GameMode;

import java.io.Serial;

public class StartGameRequest extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 356L;
    private final GameMode gameMode;

    public StartGameRequest(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
