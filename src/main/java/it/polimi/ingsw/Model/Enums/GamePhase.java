package it.polimi.ingsw.Model.Enums;

import java.io.Serial;
import java.io.Serializable;

public enum GamePhase implements Serializable {
    SETUP, ACTION;
    @Serial
    private static final long serialVersionUID = 120L; // convention: 1 for model, (01 -> 99) for objects

}
