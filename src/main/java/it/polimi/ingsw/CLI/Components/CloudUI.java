package it.polimi.ingsw.CLI.Components;

import it.polimi.ingsw.Misc.Colours;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;

public class CloudUI {

    public static String draw(GameBoard ctx) {
        String clouds = "";
        switch (ctx.getClouds().size()) {
            case 2 -> {
                String twoClouds = "";
                for (Cloud c : ctx.getClouds()) {
                    String students = "";

                    for (PawnColour p : c.getContents()) {
                        students = students + Colours.colorizeStudent(p, "\t■");
                        students = students + "\t\n";
                    }
                    if (students == "") {
                        for (int i = 0; i < 4; i++) {
                            students = students + "\t\t\n";
                        }
                    }
                    twoClouds = twoClouds + "Nuvola " + c.getId() + "\n" + students;
                }
                clouds = twoClouds + "\n\n\n";
            }

            case 3 -> {
                String threeClouds = "";
                threeClouds = threeClouds + "Nuvola " + ctx.getClouds().get(0).getId() + "\t"
                        + "Nuvola " + ctx.getClouds().get(1).getId() + "\n";
                for (int i = 0; i < 3; i++) {
                    threeClouds = threeClouds +
                            (ctx.getClouds().get(0).getContents().size() > 0 ?
                            Colours.colorizeStudent(ctx.getClouds().get(0).getContents().get(i), "\t■\t") :
                            "\t\t");
                    threeClouds = threeClouds +
                            (ctx.getClouds().get(1).getContents().size() > 0 ?
                                    Colours.colorizeStudent(ctx.getClouds().get(1).getContents().get(i), "\t■") + "\n":
                                    "\t\t\n");
                }
                threeClouds = threeClouds + "Nuvola " + ctx.getClouds().get(2).getId() + "\n";

            for (int i = 0; i < 3; i++) {
                    threeClouds = threeClouds +
                            (ctx.getClouds().get(2).getContents().size() > 0 ?
                                    Colours.colorizeStudent(ctx.getClouds().get(2).getContents().get(i), "\t■") + "\n" :
                                    "\t\t");
                }
                clouds = threeClouds + "\n\n\n";
            }

            case 4 -> {
                String fourClouds = "";
                fourClouds = fourClouds + "Nuvola " + ctx.getClouds().get(0).getId() + "\t"
                        + "Nuvola " + ctx.getClouds().get(1).getId() + "\n";
                for (int i = 0; i < 3; i++) {
                    fourClouds = fourClouds +
                            (ctx.getClouds().get(0).getContents().size() > 0 ?
                            Colours.colorizeStudent(ctx.getClouds().get(0).getContents().get(i), "\t■\t") :
                            "\t\t");
                    fourClouds = fourClouds +
                            (ctx.getClouds().get(1).getContents().size() > 0 ?
                                    Colours.colorizeStudent(ctx.getClouds().get(1).getContents().get(i), "\t■") + "\n":
                                    "\t\t\n");
                }
                fourClouds = fourClouds + "Nuvola " + ctx.getClouds().get(2).getId() + "\t"
                        + "Nuvola " + ctx.getClouds().get(3).getId() + "\n";
                for (int i = 0; i < 3; i++) {
                    fourClouds = fourClouds +
                            (ctx.getClouds().get(2).getContents().size() > 0 ?
                                    Colours.colorizeStudent(ctx.getClouds().get(2).getContents().get(i), "\t■\t") :
                                    "\t\t");
                    fourClouds = fourClouds +
                            (ctx.getClouds().get(3).getContents().size() > 0 ?
                                    Colours.colorizeStudent(ctx.getClouds().get(3).getContents().get(i), "\t■") + "\n" :
                                    "\t\t\n");
                }
                clouds = fourClouds + "\n\n\n";
            }
        }
        return clouds;
    }
}
