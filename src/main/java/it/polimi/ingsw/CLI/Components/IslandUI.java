package it.polimi.ingsw.CLI.Components;

import it.polimi.ingsw.Misc.Colours;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.IslandGroup;

import java.util.ArrayList;

/**
 * This UI component should draw a representation of the island including its ID value and the students and towers on it. <br>
 * The students will be ordered by frequency on the island to better highlight the influence. <br>
 * Mother Nature is represented by an asterisk
 */
public class IslandUI {

    public static String draw(IslandGroup i, GameBoard ctx) {
        String mn = ctx.getMutableIslandField().getMutableMotherNaturePosition().equals(i) ? "*" : "";

        String students = "";
        ArrayList<PawnColour> sortedStudents = Utils.sortByFrequency(i.getStudents());
        for (PawnColour p : sortedStudents) {
            students = students + Colours.colorizeStudent(p, "■ ");
        }

        int maxStudents = 0;
        for (IslandGroup ig : ctx.getMutableIslandField().getMutableGroups()) {
            maxStudents = Math.max(maxStudents, ig.getStudents().size());
        }

        if (i.getStudents().size() < maxStudents) {
            students = students + "  ".repeat(maxStudents - i.getStudents().size());
        }
        String tower = "";
        if (i.getTowerColour().isPresent()) {
            switch (i.getTowerColour().get()) {
                case BLACK -> tower = tower + "\tB(" + i.getTowerCount() + ")";
                case WHITE -> tower = tower + "\tW(" + i.getTowerCount() + ")";
                case GRAY -> tower = tower + "\tG(" + i.getTowerCount() + ")";
            }
        }
        return mn + " Isola " + i.getId() + ":\t" + students + tower;
    }
}


