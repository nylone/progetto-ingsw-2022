package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Misc.Symbols;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

/**
 * PlayerBoardUI allows to print all the information representing the {@link PlayerBoard}.
 * <br>
 * It exposes multiple methods to render the individual components independently and one method to combine them all
 * in a single component.
 */
public class PlayerBoardUI {

    /**
     * It draws a representation of the {@link PlayerBoard} of the provided player.
     *
     * @param playerBoard the {@link PlayerBoard} which will be represented
     * @param ctx         reference to the model used to check the available coins left in the game
     * @return the complete player board UI component
     */
    public static String drawPlayerBoard(PlayerBoard playerBoard, GameBoard ctx) {
        String screen = "";
        // Playerboard sections' titles. Change the argument of repeat() to further separate islands from playerboards
        screen = screen + "\n".repeat(1) + "Entrance:\t" + "Dining Room:\t\t" + "Teachers:\t" + "Towers:\t\t";
        // Coins should be printed only if game mode is advanced
        if (ctx.getGameMode() == GameMode.ADVANCED) {
            screen = screen + "Coins available:" + playerBoard.getCoinBalance() + "\n";
        } else {
            screen = screen + "\n";
        }

        String entrance = PlayerBoardUI.drawEntrance(playerBoard, ctx);
        String towers = PlayerBoardUI.drawTowers(playerBoard, ctx);
        for (PawnColour p : PawnColour.values()) {
            // This will print just one line of the entrance UI component
            screen = screen + entrance.substring(0, entrance.indexOf('\n'));
            entrance = entrance.substring(entrance.indexOf('\n') + 1);
            // This will print one row of the dining room
            screen = screen + "\t\t" + PlayerBoardUI.drawDiningRoomRow(p, playerBoard, ctx.getGameMode());
            // This will print one teacher per row if present
            screen = screen + "\t   " + PlayerBoardUI.drawTeacher(p, playerBoard, ctx);

            // This will print just one line of the towers UI component
            screen = screen + "\t\t  " + towers.substring(0, towers.indexOf('\n') + 1);
            towers = towers.substring(towers.indexOf("\n") + 1);
        }
        return screen;
    }

    /**
     * It draws a representation of the entrance of the provided player.
     *
     * @param pb the {@link PlayerBoard} to which the entrance should be associated with
     * @param gb reference to the model used to add padding in the entrance when 2 or 4 players are in the game
     *           because they have less students in the entrance than 3 players' game
     * @return the unused students in a multiline dual column layout String representation
     */
    public static String drawEntrance(PlayerBoard pb, GameBoard gb) {
        String entrance = "  "; // the first place is empty because of odd number of students in an even grid
        // Print the content of every place in the entrance
        for (Optional<PawnColour> p : pb.getEntranceStudents()) {
            if (p.isPresent())
                entrance = entrance + Symbols.colorizeStudent(p.get(), Symbols.PAWN) + " ";
            else entrance = entrance + "  ";

            // Every two students there should be a new line instead of a white space to force the dual column layout
            if (Symbols.stripFromANSICodes(entrance).length() % 4 == 0) {
                entrance = entrance.substring(0, entrance.length() - 1); // remove space after pawn in the II column
                entrance = entrance + "\n";
            }
        }
        // Adds padding if not enough students are present to complete the five rows layout
        if (gb.getMutablePlayerBoards().size() != 3) {
            entrance = entrance + "   " + "\n";
        }
        return entrance;
    }

    /**
     * It draws a representation of the tower storage of the provided player.
     *
     * @param p  the {@link PlayerBoard} to which the towers should be associated with
     * @param gb reference to the model used to check the relationship between towers and player
     * @return the unused towers in a multiline dual column layout String representation
     */
    public static String drawTowers(PlayerBoard p, GameBoard gb) {
        String towers = "";
        String towerColour = "";
        switch (gb.getTeamMapper().getMutableTowerStorage(p).getColour()) {
            case BLACK -> towerColour = Symbols.BLACK;
            case GRAY -> towerColour = Symbols.GRAY;
            case WHITE -> towerColour = Symbols.WHITE;
        }
        for (int i = 0; i < 8; i++) {
            // prints a tower if there is still any to print
            if (i < gb.getTeamMapper().getMutableTowerStorage(p).getTowerCount()) {
                towers = towers + Symbols.colour(Symbols.TOWER, towerColour) + " ";
            } else towers = towers + "  "; // adds whitespaces in the remaining empty spaces

            // Every two towers there should be a new line instead of a white space to force the dual column layout
            if (Symbols.stripFromANSICodes(towers).length() % 4 == 0) {
                towers = towers.substring(0, towers.length() - 1); // remove space after tower in the II column
                towers = towers + "\n";
            }
        }
        return towers + "\t\t\n";
    }

    /**
     * It draws a dining room's row with its related students and the not yet obtained coins
     *
     * @param rowColour the dining room's row which should be printed
     * @param p         the {@link PlayerBoard} to which the dining room should be associated with
     * @param gameMode  reference to the model used to add coin representation if in correct settings
     * @return a fixed length line containing all the students on the specific dining room's row
     */
    public static String drawDiningRoomRow(PawnColour rowColour, PlayerBoard p, GameMode gameMode) {
        String diningRoom = "";
        // Fills the row with the 10 elements which could be students or empty spaces
        for (int i = 0; i < 9; i++) {
            if (i < p.getDiningRoomCount(rowColour)) { // prints a student if there is still any to print
                diningRoom = diningRoom + Symbols.colorizeStudent(rowColour, Symbols.PAWN + " ");
            } else {
                // It adds coins in the III, VI and IX positions if the game mode is advanced
                if (gameMode.equals(GameMode.ADVANCED) && (i + 1) % 3 == 0) {
                    diningRoom = diningRoom + Symbols.COIN + " ";
                } else diningRoom = diningRoom + "  "; // adds whitespaces in the remaining empty places
            }
        }
        return diningRoom;
    }

    /**
     * A single teacher will be represented with the standard UI representation of the pawn piece or
     * an empty space if the player has not conquered that specific teacher yet.
     *
     * @param teacher the specific {@link PawnColour} of the teacher to be represented
     * @param p       the {@link PlayerBoard} to which the teacher should be associated with
     * @param gb      reference to the model used to check the relationship between teacher and player
     * @return single line containing teacher
     */
    public static String drawTeacher(PawnColour teacher, PlayerBoard p, GameBoard gb) {
        if (gb.getOwnTeachers(p).contains(teacher)) {
            return Symbols.colorizeStudent(teacher, Symbols.PAWN + " ");
        } else return "  ";
    }
}
