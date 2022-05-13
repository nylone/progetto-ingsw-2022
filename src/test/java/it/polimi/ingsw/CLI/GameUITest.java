package it.polimi.ingsw.CLI;


import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.TeamID;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.IslandGroup;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class GameUITest {

    @Test
    public void shouldRenderScreen() throws InvalidContainerIndexException, EmptyContainerException {
        GameBoard gb = new GameBoard(GameMode.SIMPLE, "ale", "teo", "ari", "eriantys");
        for (IslandGroup ig : gb.getMutableIslandField().getMutableGroups()) {
            for (int i = 0; i < new Random().nextInt(15); i++) {
                ig.getMutableIslands().get(0).addStudent(Utils.random(List.of(PawnColour.values())));
            }
            ig.swapTower(gb.getTeamMap().getMutableTowerStorage(Utils.random(List.of(TeamID.ONE, TeamID.TWO))));
        }
        Utils.random(gb.getClouds()).extractContents();
        System.out.println(GameUI.draw(gb));
    }

}