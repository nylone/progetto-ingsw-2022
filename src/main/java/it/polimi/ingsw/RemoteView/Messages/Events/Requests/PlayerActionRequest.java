package it.polimi.ingsw.RemoteView.Messages.Events.Requests;

import it.polimi.ingsw.Controller.Actions.PlayerAction;

import java.io.Serial;

public class PlayerActionRequest extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 355L;
    private final PlayerAction playerAction;

    public PlayerActionRequest(PlayerAction action) {
        this.playerAction = action;
    }

    public PlayerAction getAction() {
        return playerAction;
    }
}
