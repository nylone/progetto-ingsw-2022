package it.polimi.ingsw.RemoteView.Messages.Events.Internal;

import it.polimi.ingsw.Model.ModelReader;
import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;

public class ModelUpdateEvent implements ClientEvent {
    private final ModelReader update;

    public ModelUpdateEvent(ModelReader update) {
        this.update = update;
    }

    public ModelReader getModel() {
        return update;
    }
}
