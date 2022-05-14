package it.polimi.ingsw.RemoteView.Messages.Events;

import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class ModelUpdate extends ClientEvent {
    private final GameBoard update;

    public ModelUpdate(GameBoard update) {
        this.update = update;
    }

    public GameBoard getModel() {
        return update;
    }
}
