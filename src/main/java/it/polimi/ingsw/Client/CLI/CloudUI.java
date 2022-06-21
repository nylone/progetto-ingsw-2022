package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Misc.Symbols;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Model;

/**
 * CloudUI is a graphical representation (as a String data structure) useful to print multiple cloud components
 * on the console.
 * <br>
 * The internal information of the model element is described in {@link it.polimi.ingsw.Model.Cloud}.
 */
public class CloudUI {

    /**
     * The {@link it.polimi.ingsw.Model.Cloud} will be represented with a title and its related id,
     * and the contained students if present.
     * According to the number of players, it will create a different layout of the clouds on the console.
     *
     * @param ctx is a reference to the model used to access the clouds' information.
     * @return the representation of the clouds group.
     */
    public static String draw(Model ctx) {
        String clouds = "";
        switch (ctx.getClouds().size()) {
            case 2 -> { // two clouds will be print on top of each other
                StringBuilder twoClouds = new StringBuilder();
                for (Cloud c : ctx.getClouds()) {
                    StringBuilder students = new StringBuilder();

                    // Prints one line for every student on the cloud, and aligns it centrally
                    for (PawnColour p : c.getContents()) {
                        students.append(Symbols.colorizeStudent(p, "\t" + Symbols.PAWN));
                        students.append("\t\n");
                    }
                    // Handles the empty cloud case
                    if (students.toString().equals("")) {
                        students.append("\t\t\n".repeat(3));
                    }
                    // Appends the current cloud in the loop.
                    // Adds the current cloud title on a line.
                    // Adds the content on the cloud on separate lines.
                    twoClouds.append("\tCloud ").append(c.getId() + 1).append("\n").append(students);
                }
                clouds = twoClouds.toString();
            }

            case 3 -> { // three clouds will be print: the first pair next to each other, the third under the first
                StringBuilder threeClouds = new StringBuilder();
                threeClouds.append("Cloud ").append(ctx.getClouds().get(0).getId() + 1).append("\t\t" // divider between clouds
                ).append("Cloud ").append(ctx.getClouds().get(1).getId() + 1).append("\n"); // title of cloud 2
                for (int i = 0; i < 3; i++) { // prints the content of the I and II clouds next to each other if present
                    threeClouds.append(ctx.getClouds().get(0).getContents().size() > 0 ?
                            Symbols.colorizeStudent(ctx.getClouds().get(0).getContents().get(i), "\t" +
                                    Symbols.PAWN + "\t\t") : "\t\t\t");
                    threeClouds.append(ctx.getClouds().get(1).getContents().size() > 0 ?
                            Symbols.colorizeStudent(ctx.getClouds().get(1).getContents().get(i), "\t" +
                                    Symbols.PAWN) + "\n" : "\t\t\n");
                }
                threeClouds.append("Cloud ").append(ctx.getClouds().get(2).getId() + 1).append("\n"); // title of cloud 3
                for (int i = 0; i < 3; i++) { // prints the content of the III cloud if present
                    threeClouds.append(ctx.getClouds().get(2).getContents().size() > 0 ?
                            Symbols.colorizeStudent(ctx.getClouds().get(2).getContents().get(i), "\t"
                                    + Symbols.PAWN) + "\n" :
                            "\t\t\n");
                }
                clouds = threeClouds.toString();
            }

            case 4 -> { // four clouds will be print as a grid
                StringBuilder fourClouds = new StringBuilder();
                fourClouds.append("Cloud ").append(ctx.getClouds().get(0).getId() + 1).append("\t\t" // separation between clouds
                ).append("Cloud ").append(ctx.getClouds().get(1).getId() + 1).append("\n"); // title of cloud 2
                for (int i = 0; i < 3; i++) { // prints the content of the I and II clouds next to each other if present
                    fourClouds.append(ctx.getClouds().get(0).getContents().size() > 0 ?
                            Symbols.colorizeStudent(ctx.getClouds().get(0).getContents().get(i), "\t"
                                    + Symbols.PAWN + "\t\t") : "\t\t\t");
                    fourClouds.append(ctx.getClouds().get(1).getContents().size() > 0 ?
                            Symbols.colorizeStudent(ctx.getClouds().get(1).getContents().get(i), "\t"
                                    + Symbols.PAWN) + "\n" : "\t\t\n");
                }
                fourClouds.append("Cloud ").append(ctx.getClouds().get(2).getId() + 1).append("\t\t" // separation between clouds
                ).append("Cloud ").append(ctx.getClouds().get(3).getId() + 1).append("\n"); // title of cloud 4
                for (int i = 0; i < 3; i++) { // prints the content of the III and IV clouds next to each other if present
                    fourClouds.append(ctx.getClouds().get(2).getContents().size() > 0 ?
                            Symbols.colorizeStudent(ctx.getClouds().get(2).getContents().get(i), "\t"
                                    + Symbols.PAWN + "\t\t") : "\t\t\t");
                    fourClouds.append(ctx.getClouds().get(3).getContents().size() > 0 ?
                            Symbols.colorizeStudent(ctx.getClouds().get(3).getContents().get(i), "\t"
                                    + Symbols.PAWN) + "\n" :
                            "\t\t\n");
                }
                clouds = fourClouds.toString();
            }
        }
        // Multiple newlines are used in conjunction with IslandUI component.
        // If there are more islands than CloudUI component's lines we still want to print the islands stacked
        // in GameUI.
        return clouds + "\n\n\n";
    }
}
