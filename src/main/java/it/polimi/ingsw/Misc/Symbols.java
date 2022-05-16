package it.polimi.ingsw.Misc;

import it.polimi.ingsw.Model.Enums.PawnColour;

public class Symbols {
    public static String BLACK = "\u001b[30m";
    public static String WHITE = "\u001b[243m";
    public static String GRAY = "\u001b[37m";
    public static String BLUE = "\u001b[34m";
    public static String GREEN = "\u001b[32m";
    public static String PINK = "\u001b[35m";
    public static String RED = "\u001b[31m";
    public static String YELLOW = "\u001b[33m";
    public static String RESET = "\u001b[00m";

    public static String PAWN = "■";
    public static String TOWER = "≡";


    public static String colorizeStudent(PawnColour p, String message) {
        String student = "";
        switch (p) {
            case BLUE -> student = student + Symbols.colour(message, Symbols.BLUE);
            case GREEN -> student = student + Symbols.colour(message, Symbols.GREEN);
            case PINK -> student = student + Symbols.colour(message, Symbols.PINK);
            case RED -> student = student + Symbols.colour(message, Symbols.RED);
            case YELLOW -> student = student + Symbols.colour(message, Symbols.YELLOW);
        }
        return student;
    }


    public static String colour(String s, String colour) {
        return colour + s + Symbols.RESET;
    }

    public static String stripFromANSICodes(String s) {
        return s
                .replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "") // removes all ANSI control codes
                .replaceAll("(\t)+", "    "); // converts tabs to four spaces
    }
}