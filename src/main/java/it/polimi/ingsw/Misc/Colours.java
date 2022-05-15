package it.polimi.ingsw.Misc;

import it.polimi.ingsw.Model.Enums.PawnColour;

public class Colours {
    public static String BLUE = "\u001b[34m";
    public static String GREEN = "\u001b[32m";
    public static String PINK = "\u001b[35m";
    public static String RED = "\u001b[31m";
    public static String YELLOW = "\u001b[33m";
    public static String RESET = "\u001b[00m";

    public static String colorizeStudent(PawnColour p, String message) {
        String student = "";
        switch (p) {
            case BLUE -> student = student + Colours.colour(message, Colours.BLUE);
            case GREEN -> student = student + Colours.colour(message, Colours.GREEN);
            case PINK -> student = student + Colours.colour(message, Colours.PINK);
            case RED -> student = student + Colours.colour(message, Colours.RED);
            case YELLOW -> student = student + Colours.colour(message, Colours.YELLOW);
        }
        return student;
    }

    public static String colour(String s, String colour) {
        return colour + s + Colours.RESET;
    }
}
