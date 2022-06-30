package it.polimi.ingsw.Server.Messages.Events.Requests;

import it.polimi.ingsw.Controller.Actions.PlayerAction;

import java.io.Serial;

/**
 * Represents a Client attempting to run a {@link PlayerAction} on the game
 */
public class PlayerActionRequest extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 355L;
    private final PlayerAction playerAction;

    /**
     * Construct the request
     * @param action the action the user wishes to run
     */
    public PlayerActionRequest(PlayerAction action) {
        this.playerAction = action;
    }

    /**
     * Get the user defined action
     * @return a {@link PlayerAction} to run on a {@link it.polimi.ingsw.Controller.Controller}
     */
    public PlayerAction getAction() {
        return playerAction;
    }
}
