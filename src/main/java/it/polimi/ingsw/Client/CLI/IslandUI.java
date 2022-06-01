package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Misc.Symbols;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.IslandGroup;

import java.util.ArrayList;

/**
 * IslandUI allows to print a placeholder containing an {@link IslandGroup} representation or an empty row
 * which could be needed if there was previously an IslandGroup on the console which does not exist anymore
 * in the model
 */
public class IslandUI {

    /**
     * The {@link IslandGroup} will be represented with its ID value and the students and towers on it.
     * <br>
     * The students will be ordered by frequency on the island to better highlight the influence.
     * <br>
     * Mother Nature is represented by an asterisk
     * <br>
     * The number of tower is shown via a number
     *
     * @param i   the subject to be represented
     * @param ctx reference to the model used to identify the presence of the mother nature piece on the island group
     * @return the representation of an island group.
     */
    public static String draw(IslandGroup i, GameBoard ctx) {
        // Adds an identifier if the island contains MotherNature
        String mn = ctx.getMutableIslandField().getMutableMotherNaturePosition().equals(i) ? "*" : "";

        String students = "";
        // Prints each student on the island
        ArrayList<PawnColour> sortedStudents = Utils.sortByFrequency(i.getStudents());
        for (PawnColour p : sortedStudents) {
            students = students + Symbols.colorizeStudent(p, Symbols.PAWN + " ");
        }

        // Adds padding if the current island doesn't hold the most students
        int maxStudents = 0;
        for (IslandGroup ig : ctx.getMutableIslandField().getMutableGroups()) {
            maxStudents = Math.max(maxStudents, ig.getStudents().size());
        }
        if (i.getStudents().size() < maxStudents) {
            students = students + "  ".repeat(maxStudents - i.getStudents().size()); // padding
        }

        // Prints the tower representation accounting for team and quantity
        String tower = "";
        if (i.getTowerColour().isPresent()) {
            switch (i.getTowerColour().get()) {
                case BLACK -> tower = tower + "\t" + Symbols.colour(Symbols.TOWER + " ", Symbols.BLACK)
                        + i.getTowerCount();
                case GRAY -> tower = tower + "\t" + Symbols.colour(Symbols.TOWER + " ", Symbols.GRAY)
                        + i.getTowerCount();
                case WHITE -> tower = tower + "\t" + Symbols.colour(Symbols.TOWER + " ", Symbols.WHITE)
                        + i.getTowerCount();

            }
        } else tower = "\t" + " ".repeat(4);

        String islandTitle = "Island " + i.getId(); // island's title and id
        if (i.getMutableNoEntryTiles().size() > 0) {
            // The title will be crossed out if the island's influence cannot be changed
            islandTitle = Symbols.STRIKED + islandTitle + Symbols.RESET + Symbols.BACKGROUND;
        }
        return mn + islandTitle + ":\t" + students + tower;
    }

    /**
     * It basically provides the same result as {@link IslandUI#draw(IslandGroup, GameBoard)} but it replaces
     * all characters with whitespaces (except tabulations)
     *
     * @param ctx reference to the model used to identify the island group containing the most students
     * @return an all whitespace's filled String, long enough to fill the IslandUIs' composition
     */
    public static String drawEmptyRow(GameBoard ctx) {
        String mn = "";

        String islandTitle = " ".repeat(10);

        String students = "";

        int maxStudents = 0;
        for (IslandGroup ig : ctx.getMutableIslandField().getMutableGroups()) {
            maxStudents = Math.max(maxStudents, ig.getStudents().size());
        }
        students = students + "  ".repeat(maxStudents);

        String tower = "\t" + " ".repeat(4);

        return mn + islandTitle + " \t" + students + tower;
    }
}


