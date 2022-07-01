package it.polimi.ingsw.Server.Messages.Events.Requests;

import it.polimi.ingsw.Model.Enums.GameMode;

import java.io.Serial;

/**
 * Represents a Client triying to start a game in a {@link it.polimi.ingsw.Server.Lobby}
 */
public class StartGameRequest extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 356L;
    private final GameMode gameMode;

    /**
     * Construct the request
     *
     * @param gameMode a variant of {@link GameMode} to select the rules of the game to apply (simplified or advanced)
     */
    public StartGameRequest(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Get the selected game mode
     *
     * @return a variant of {@link GameMode} to select the rules of the game to apply (simplified or advanced)
     */
    public GameMode getGameMode() {
        return gameMode;
    }
}
