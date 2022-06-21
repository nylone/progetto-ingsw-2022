package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Misc.Symbols;
import it.polimi.ingsw.Model.Model;

/**
 * GameUI is a graphical representation (as a String data structure)
 * useful to print {@link it.polimi.ingsw.Client.CLI.CloudUI}
 * and multiple {@link it.polimi.ingsw.Client.CLI.IslandUI} components
 * next to each other on the console.
 */
public class GameUI {

    /**
     * The console will be populated with all the island groups in the island field followed by the clouds on
     * the same multiline block.
     *
     * @param ctx is a reference to the model used to access the {@link it.polimi.ingsw.Model.IslandField} to print
     *            the {@link it.polimi.ingsw.Client.CLI.IslandUI} and passed to the
     *            {@link it.polimi.ingsw.Client.CLI.CloudUI}
     * @return a composed view of islands and clouds
     */
    public static String draw(Model ctx) {
        // The background colour helpful to enhance contrast between black towers and the default black background console
        StringBuilder screen = new StringBuilder(Symbols.BACKGROUND);

        String clouds = CloudUI.draw(ctx); // draws the cloud component which will be stripped and printed line by line
        int groupsSize = ctx.getMutableIslandField().getMutableGroups().size();
        // The number of rows of the UI component depends on the number of island groups.
        // If there are less island groups than the number of CloudUI's lines, padding should be added in the form of
        // empty rows to replace the IslandUI component.
        // The constant '8' is the minimum number of lines/IslandUIs needed to correctly display the cloud component.
        int rows = Math.max(groupsSize, 8);
        for (int i = 0; i < rows; i++) { // on each row will be printed an IslandUI and one line of CloudUI
            // on every line prints the island group but if there are none to print, it fills the space with
            // an empty line
            if (i < ctx.getMutableIslandField().getMutableGroups().size()) {
                String currentIsland = IslandUI.draw(ctx.getMutableIslandField().getMutableGroups().get(i), ctx);
                screen.append(currentIsland).append("\t".repeat(2)); // '\t' is used for horizontal separation between islands and clouds
            } else screen.append(IslandUI.drawEmptyRow(ctx)).append("\t".repeat(2));

            // This will print just one line of the clouds UI component
            screen.append(clouds, 0, clouds.indexOf('\n') + 1);
            clouds = clouds.substring(clouds.indexOf('\n') + 1);
        }

        return screen.toString();
    }
}
