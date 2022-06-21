package it.polimi.ingsw.Model.Enums;

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

    /**
     * Convert a string to a valid PawnColour
     *
     * @param text string containing presumably a pawnColour
     * @return PawnColour or null whether the string does not represent a valid pawnColour
     */
    public static PawnColour getPawnColourFromText(String text) {
        switch (text) {
            case "RED" -> {
                return PawnColour.RED;
            }
            case "BLUE" -> {
                return PawnColour.BLUE;
            }
            case "PINK" -> {
                return PawnColour.PINK;
            }
            case "YELLOW" -> {
                return PawnColour.YELLOW;
            }
            case "GREEN" -> {
                return PawnColour.GREEN;
            }
        }
        return null;
    }
}
