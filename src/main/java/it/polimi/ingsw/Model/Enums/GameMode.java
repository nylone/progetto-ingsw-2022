package it.polimi.ingsw.Model.Enums;

import java.io.Serial;
import java.io.Serializable;

public enum GameMode implements Serializable {

    SIMPLE, ADVANCED;
    @Serial
    private static final long serialVersionUID = 119L; // convention: 1 for model, (01 -> 99) for objects
}
