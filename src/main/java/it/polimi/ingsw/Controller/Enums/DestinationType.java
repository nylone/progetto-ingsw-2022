package it.polimi.ingsw.Controller.Enums;

import java.io.Serial;
import java.io.Serializable;

public enum DestinationType implements Serializable {
    DININGROOM,
    ISLAND;
    @Serial
    private static final long serialVersionUID = 208L; // convention: 2 for controller, (01 -> 99) for objects
}
