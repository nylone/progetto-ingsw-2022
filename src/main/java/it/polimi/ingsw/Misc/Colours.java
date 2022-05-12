package it.polimi.ingsw.Misc;

public class Colours {
    public static String BLUE = "\u001b[34m";
    public static String GREEN = "\u001b[32m";
    public static String PINK = "\u001b[35m";
    public static String RED = "\u001b[31m";
    public static String YELLOW = "\u001b[33m";
    public static String RESET = "\u001b[0m";

    public static String colour(String s, String colour) {
        return colour + s + Colours.RESET;
    }
}
