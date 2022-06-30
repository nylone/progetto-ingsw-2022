package it.polimi.ingsw.Server.Messages.Events.Internal;

import it.polimi.ingsw.Server.Messages.Events.ClientEvent;

/**
 * This Event is generated when a {@link it.polimi.ingsw.Network.SocketWrapper} gets closed.
 * Receiving this method means the socket wrapper object is no longer functioning.
 */
public class SocketClosedEvent implements ClientEvent { }
