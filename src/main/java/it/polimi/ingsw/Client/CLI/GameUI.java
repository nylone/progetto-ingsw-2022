package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Misc.Symbols;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.IslandGroup;

public class GameUI {

    public static String draw(GameBoard ctx) throws InvalidContainerIndexException, EmptyContainerException {
        String screen = Symbols.BACKGROUND;

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
}
