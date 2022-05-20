package it.polimi.ingsw.RemoteView.Messages.Events.Internal;

import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;

public class ModelUpdateEvent implements ClientEvent {
    private final GameBoard update;

    public ModelUpdateEvent(GameBoard update) {
        this.update = update;
    }

    public GameBoard getModel() {
        return update;
    }
}
