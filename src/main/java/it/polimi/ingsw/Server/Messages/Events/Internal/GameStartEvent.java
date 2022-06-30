package it.polimi.ingsw.Server.Messages.Events.Internal;

import it.polimi.ingsw.Model.ModelWrapper;
import it.polimi.ingsw.Server.Messages.Events.ClientEvent;

import java.util.Map;

/**
 * This Event is generated when a lobby's game is started. It is always spawned BEFORE a {@link ModelUpdateEvent}
 * in the {@link ModelWrapper#editModel} method
 */
public record GameStartEvent(Map<String, Integer> nickToID) implements ClientEvent {
    /**
     * Creates the event
     * @param nickToID a mapping from the nickname of a player to its {@link it.polimi.ingsw.Model.PlayerBoard}'s ID in the
     *                 {@link it.polimi.ingsw.Model.Model}
     */
    public GameStartEvent(Map<String, Integer> nickToID) {
        this.nickToID = Map.copyOf(nickToID);
    }
}
