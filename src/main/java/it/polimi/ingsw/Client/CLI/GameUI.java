package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Misc.Symbols;
import it.polimi.ingsw.Model.GameBoard;

public class GameUI {

    public static String draw(GameBoard ctx) throws InvalidContainerIndexException, EmptyContainerException {
        String screen = Symbols.BACKGROUND;

        int line = 0;
        String clouds = CloudUI.draw(ctx);
        int groupsSize = ctx.getMutableIslandField().getMutableGroups().size();
        int rows = groupsSize < 8 ? 8 : groupsSize;
        for (int i = 0; i < rows; i++) {
            if (i < ctx.getMutableIslandField().getMutableGroups().size()) {
                String currentIsland = IslandUI.draw(ctx.getMutableIslandField().getMutableGroups().get(i), ctx);
                screen = screen + currentIsland + "\t".repeat(2); // '\t' is used for horizontal separation between islands and clouds
            } else screen = screen + IslandUI.drawEmptyRow(ctx) + "\t".repeat(2);

            // This will print just one line of the clouds UI component
            screen = screen + clouds.substring(0, clouds.indexOf('\n') + 1);
            clouds = clouds.substring(clouds.indexOf('\n') + 1);
        }

        return screen;
    }
}
