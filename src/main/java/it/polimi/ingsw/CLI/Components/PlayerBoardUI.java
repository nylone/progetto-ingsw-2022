package it.polimi.ingsw.CLI.Components;

import it.polimi.ingsw.Misc.Symbols;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

public class PlayerBoardUI {

    public static String drawEntrance(PlayerBoard pb, GameBoard gb) {
        String entrance = "  ";
        for (Optional<PawnColour> p : pb.getEntranceStudents()) {
            if (p.isPresent())
                entrance = entrance + Symbols.colorizeStudent(p.get(), Symbols.PAWN) + " ";
            else entrance = entrance + "  ";

            if (Symbols.stripFromANSICodes(entrance).length() % 4 == 0) {
                entrance = entrance.substring(0, entrance.length() - 1); // remove space after pawn in the II column
                entrance = entrance + "\n";
            }
        }
        if (gb.getMutablePlayerBoards().size() != 3) {
            entrance = entrance + "   " + "\n";
        }
        return entrance;
    }

    public static String drawDiningRoomRow(PlayerBoard p, PawnColour pc) {
        String diningRoom = "";
        for (int i = 0; i < 9; i++) {
            if (i < p.getDiningRoomCount(pc))
                diningRoom = diningRoom + Symbols.colorizeStudent(pc, Symbols.PAWN + " ");
            else diningRoom = diningRoom + "  ";
        }
        return diningRoom;
    }

    public static String drawTeacher(PawnColour teacher, PlayerBoard p, GameBoard gb) {
            if (gb.getOwnTeachers(p).contains(teacher)) {
                return Symbols.colorizeStudent(teacher, Symbols.PAWN + " ");
            }
            else return "  ";
    }

    public static String drawTowers(PlayerBoard p, GameBoard gb) {
        String towers = "";
        String towerColour = "";
        switch (gb.getTeamMap().getMutableTowerStorage(p).getColour()) {
            case BLACK -> towerColour = Symbols.BLACK;
            case GRAY -> towerColour = Symbols.GRAY;
            case WHITE -> towerColour = Symbols.WHITE;
        }
        for (int i = 0; i < 8; i++) {
            if (i < gb.getTeamMap().getMutableTowerStorage(p).getTowerCount()) {
                towers = towers + Symbols.colour(Symbols.TOWER, towerColour) + " ";
            }
            else towers = towers + "  ";

            if (Symbols.stripFromANSICodes(towers).length() % 4 == 0) {
                towers = towers.substring(0, towers.length() - 1); // remove space after tower in the II column
                towers = towers + "\n";
            }
        }
        return towers + "\t\t\n";
    }
}
