package it.polimi.ingsw.Model;

import java.io.Serial;
import java.io.Serializable;

public enum PawnColour implements Serializable {
    BLUE,
    GREEN,
    PINK,
    RED,
    YELLOW;
    @Serial
    private static final long serialVersionUID = 125L; // convention: 1 for model, (01 -> 99) for objects
}
