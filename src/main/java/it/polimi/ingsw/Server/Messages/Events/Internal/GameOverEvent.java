package it.polimi.ingsw.Server.Messages.Events.Internal;

import it.polimi.ingsw.Model.ModelWrapper;
import it.polimi.ingsw.Server.Messages.Events.ClientEvent;

import java.util.List;

/**
 * This Event is generated when a lobby's game is detected to have ended. It is always spawned AFTER a {@link ModelUpdateEvent}
 * in the {@link ModelWrapper#editModel} method
 */
public record GameOverEvent(List<String> winners) implements ClientEvent {}
