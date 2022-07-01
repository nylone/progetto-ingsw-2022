package it.polimi.ingsw.Misc;

import it.polimi.ingsw.Model.Enums.PawnColour;

/**
 * Symbols holds constants and methods used by the CLI. Mainly it focuses on colour management via ANSI codes
 */
public class Symbols {
    public static final String BLACK = "\u001b[30m";
    public static final String WHITE = "\u001b[243m";
    public static final String GRAY = "\u001b[37m";
    public static final String BLUE = "\u001b[34m";
    public static final String GREEN = "\u001b[32m";
    public static final String PINK = "\u001b[35m";
    public static final String RED = "\u001b[31m";
    public static final String YELLOW = "\u001b[33m";

    // colour used to represent coins
    public static final String GOLD = "\u001b[38;5;220m";

    // code used to reset terminal characters style
    public static final String RESET = "\u001b[00m";

    // simple gray background used under all the CLI to improve visibility independently of the specific console setting
    public static final String BACKGROUND = "\u001b[48;5;237m";

    // background used to highlight students in the entrance
    public static final String BACKGROUND_BLUE = "\u001b[44m";
    public static final String BACKGROUND_GREEN = "\u001b[42m";
    public static final String BACKGROUND_PINK = "\u001b[45m";
    public static final String BACKGROUND_RED = "\u001b[41m";
    public static final String BACKGROUND_YELLOW = "\u001b[43m";

    // improves readability of certain icons (towers and students)
    public static final String BOLD = "\u001b[1m";

    // used to handle no entry tiles
    public static final String OVERLAY = "\u001b[7m";

    // icons to represent specific model entities
    public static final String PAWN = BOLD + "■" + RESET + BACKGROUND;
    public static final String TOWER = BOLD + "≡" + RESET + BACKGROUND;
    public static final String COIN = GOLD + "¤" + RESET + BACKGROUND;


    /**
     * Method used to create a visual representation of the students using a colour and the icon.
     * Extends the functionality of the method {@link Symbols#colour(String, String)}
     *
     * @param p       student object used to access colour information
     * @param message the icon or character with which the student will be represented
     * @return a String containing the ASCII code of the colour followed by the message
     */
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

    /**
     * Method used to change the style of a string. It is usually used to change the colour of the string but
     * can be used to change other aspect of the style in general
     *
     * @param s      the String of which we want to change the style
     * @param colour the ANSI code with which the String style will be modified
     * @return a String with style changed
     */
    public static String colour(String s, String colour) {
        return colour + s + Symbols.RESET + Symbols.BACKGROUND;
    }

    /**
     * Method used to create a visual representation of the students using a colour as the background of a String.
     *
     * @param p       student object used to access colour information
     * @param message the icon or character with which the student will be represented
     * @return a String containing the ASCII code of the colour followed by the message
     */
    public static String colorizeBackgroundStudent(PawnColour p, String message) {
        String student = "";
        switch (p) {
            case BLUE ->
                    student = student + Symbols.BACKGROUND_BLUE + Symbols.BLACK + " " + message + " " + Symbols.RESET + Symbols.BACKGROUND;
            case GREEN ->
                    student = student + Symbols.BACKGROUND_GREEN + Symbols.BLACK + " " + message + " " + Symbols.RESET + Symbols.BACKGROUND;
            case PINK ->
                    student = student + Symbols.BACKGROUND_PINK + Symbols.BLACK + " " + message + " " + Symbols.RESET + Symbols.BACKGROUND;
            case RED ->
                    student = student + Symbols.BACKGROUND_RED + Symbols.BLACK + " " + message + " " + Symbols.RESET + Symbols.BACKGROUND;
            case YELLOW ->
                    student = student + Symbols.BACKGROUND_YELLOW + Symbols.BLACK + " " + message + " " + Symbols.RESET + Symbols.BACKGROUND;

        }
        return student;
    }

    /**
     * Method used to remove all ANSI codes and tabulation from a String. Useful to count character within a String
     * which has its style affected by ANSI codes
     *
     * @param s the message which the ANSI code will be stripped from
     * @return a simple ASCII String
     */
    public static String stripFromANSICodes(String s) {
        return s
                .replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -/]*[@-~]", "") // removes all ANSI control codes
                .replaceAll("(\t)+", "    "); // converts tabs to four spaces
    }
}
