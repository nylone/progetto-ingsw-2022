package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.MoveStudent;

import java.io.Serial;
import java.io.Serializable;

/**
 * This enumeration is used by {@link MoveStudent} to direct a pawn from the entrance to the correct destination
 */
public enum DestinationType implements Serializable {
    /**
     * The destination is the Dining Room
     */
    DININGROOM,
    /**
     * The destination is an Island
     */
    ISLAND;
    @Serial
    private static final long serialVersionUID = 208L; // convention: 2 for controller, (01 -> 99) for objects
}
