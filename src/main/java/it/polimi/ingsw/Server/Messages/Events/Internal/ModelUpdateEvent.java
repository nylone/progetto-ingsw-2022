package it.polimi.ingsw.Server.Messages.Events.Internal;

import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Server.Messages.Events.ClientEvent;

public class ModelUpdateEvent implements ClientEvent {
    private final Model update;

    public ModelUpdateEvent(Model update) {
        this.update = update;
    }

    public Model getModel() {
        return update;
    }
}
