package it.polimi.ingsw.CLI;

import it.polimi.ingsw.CLI.Components.CloudUI;
import it.polimi.ingsw.CLI.Components.IslandUI;
import it.polimi.ingsw.CLI.Components.PlayerBoardUI;
import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.IslandGroup;
import it.polimi.ingsw.Model.PlayerBoard;

public class GameUI {

    public static String draw(GameBoard ctx) throws InvalidContainerIndexException, EmptyContainerException {
        String screen = "";

        int line = 0;
        String clouds = CloudUI.draw(ctx);
        for (IslandGroup ig : ctx.getMutableIslandField().getMutableGroups()) {
            String currentIsland = IslandUI.draw(ig, ctx);
            screen = screen + currentIsland + "\t".repeat(2); // '\t' is used for horizontal separation between islands and clouds

            // This will print just one line of the clouds UI component
            screen = screen + clouds.substring(0, clouds.indexOf('\n') + 1);
            clouds = clouds.substring(clouds.indexOf('\n') + 1);
        }

        return screen;
    }

    public static String drawPlayerBoard(PlayerBoard playerBoard, GameBoard ctx) {
        String screen = "";
        // Playerboard sections' titles. Change the argument of repeat() to further separate islands from playerboards
        screen = screen + "\n".repeat(3) + "Entrance:\t" + "Dining Room:\t" + "Teachers:\t" + "Towers:\t" + "\n";

        String entrance = PlayerBoardUI.drawEntrance(playerBoard, ctx);
        String towers = PlayerBoardUI.drawTowers(playerBoard, ctx);
        for (PawnColour p : PawnColour.values()) {
            // This will print just one line of the entrance UI component
            screen = screen + entrance.substring(0, entrance.indexOf('\n'));
            entrance = entrance.substring(entrance.indexOf('\n') + 1);
            // This will print one row of the dining room
            screen = screen + "\t\t\t" + PlayerBoardUI.drawDiningRoomRow(playerBoard, p);
            // This will print one teacher per row if present
            screen = screen + "\t" + PlayerBoardUI.drawTeacher(p, playerBoard, ctx);

            screen = screen + "\t\t  " + towers.substring(0, towers.indexOf('\n') + 1);
            towers = towers.substring(towers.indexOf("\n") + 1);
        }
        return screen;
    }
}
