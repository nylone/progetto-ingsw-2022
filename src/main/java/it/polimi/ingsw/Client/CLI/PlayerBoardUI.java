package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Misc.Symbols;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

public class PlayerBoardUI {

    public static String drawPlayerBoard(PlayerBoard playerBoard, GameBoard ctx) {
        String screen = "";
        // Playerboard sections' titles. Change the argument of repeat() to further separate islands from playerboards
        screen = screen + "\n".repeat(1) + "Entrance:\t" + "Dining Room:\t\t" + "Teachers:\t" + "Towers:\t\t";
        if(ctx.getGameMode() == GameMode.ADVANCED){
            screen = screen + "Coins available:" + playerBoard.getCoinBalance() + "\n";
        }else{
            screen = screen+ "\n";
        }

        String entrance = PlayerBoardUI.drawEntrance(playerBoard, ctx);
        String towers = PlayerBoardUI.drawTowers(playerBoard, ctx);
        for (PawnColour p : PawnColour.values()) {
            // This will print just one line of the entrance UI component
            screen = screen + entrance.substring(0, entrance.indexOf('\n'));
            entrance = entrance.substring(entrance.indexOf('\n') + 1);
            // This will print one row of the dining room
            screen = screen + "\t\t" + PlayerBoardUI.drawDiningRoomRow(playerBoard, p, ctx.getGameMode());
            // This will print one teacher per row if present
            screen = screen + "\t   " + PlayerBoardUI.drawTeacher(p, playerBoard, ctx);

            screen = screen + "\t\t  " + towers.substring(0, towers.indexOf('\n') + 1);
            towers = towers.substring(towers.indexOf("\n") + 1);
        }
        return screen;
    }

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
            } else towers = towers + "  ";

            if (Symbols.stripFromANSICodes(towers).length() % 4 == 0) {
                towers = towers.substring(0, towers.length() - 1); // remove space after tower in the II column
                towers = towers + "\n";
            }
        }
        return towers + "\t\t\n";
    }

    public static String drawDiningRoomRow(PlayerBoard p, PawnColour pc, GameMode gameMode) {
        String diningRoom = "";
        for (int i = 0; i < 9; i++) {
            if (i < p.getDiningRoomCount(pc)) {
                diningRoom = diningRoom + Symbols.colorizeStudent(pc, Symbols.PAWN + " ");
            }
            else {
                if (gameMode.equals(GameMode.ADVANCED) && (i + 1) % 3 == 0) {
                    diningRoom = diningRoom + Symbols.COIN + " ";
                }
                else diningRoom = diningRoom + "  ";
            }
        }
        return diningRoom;
    }

    public static String drawTeacher(PawnColour teacher, PlayerBoard p, GameBoard gb) {
        if (gb.getOwnTeachers(p).contains(teacher)) {
            return Symbols.colorizeStudent(teacher, Symbols.PAWN + " ");
        } else return "  ";
    }
}
