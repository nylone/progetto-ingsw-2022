package it.polimi.ingsw.Model;

import java.io.Serial;
import java.io.Serializable;

public enum StateType implements Serializable {
    PAWNCOLOUR,
    NOENTRY;
    @Serial
    private static final long serialVersionUID = 129L; // convention: 1 for model, (01 -> 99) for objects
}
