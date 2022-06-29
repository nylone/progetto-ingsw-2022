package it.polimi.ingsw.Model.Enums;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents the game mode of the game. Can be either {@link #SIMPLE} or {@link #ADVANCED}
 */
public enum GameMode implements Serializable {
    SIMPLE,
    ADVANCED;
    @Serial
    private static final long serialVersionUID = 119L; // convention: 1 for model, (01 -> 99) for objects
}
