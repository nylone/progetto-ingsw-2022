package it.polimi.ingsw.Model.Enums;

import java.awt.*;
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
     * @param text string containing presumably a pawnColour
     * @return PawnColour or null whether the string does not represent a valid pawnColour
     */
    public static PawnColour getPawnColourFromText(String text){
        switch (text){
            case "red" -> {
                return PawnColour.RED;
            }
            case "blue" -> {
                return PawnColour.BLUE;
            }
            case "pink" -> {
                return PawnColour.PINK;
            }case "yellow" -> {
                return PawnColour.YELLOW;
            }
            case "green" -> {
                return PawnColour.GREEN;
            }
        }
        return null;
    }
}
