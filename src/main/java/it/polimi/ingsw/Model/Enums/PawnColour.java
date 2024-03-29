package it.polimi.ingsw.Model.Enums;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents the colour of many pawns on the game boards, like Students and Teachers.
 */
public enum PawnColour implements Serializable {
    GREEN,
    RED,
    YELLOW,
    PINK,
    BLUE;
    @Serial
    private static final long serialVersionUID = 125L; // convention: 1 for model, (01 -> 99) for objects

    /**
     * Convert a string to a valid PawnColour
     *
     * @param text string containing a colour
     * @return the appropriate PawnColour or null when the string does not represent a valid pawnColour
     */
    public static PawnColour getPawnColourFromText(String text) {
        switch (text.toUpperCase().trim()) {
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
