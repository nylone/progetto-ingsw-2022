package it.polimi.ingsw.CLI;

import it.polimi.ingsw.Client.CLI.GameUI;
import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.FullContainerException;
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
    public void shouldRenderScreen() throws InvalidContainerIndexException, EmptyContainerException, FullContainerException {
        GameBoard gb = new GameBoard(GameMode.SIMPLE, "ale", "teo", "ari");
        for (IslandGroup ig : gb.getMutableIslandField().getMutableGroups()) {
            for (int i = 0; i < new Random().nextInt(15); i++) {
                ig.getMutableIslands().get(0).addStudent(Utils.random(List.of(PawnColour.values())));
            }
            ig.swapTower(gb.getTeamMapper().getMutableTowerStorage(Utils.random(List.of(TeamID.ONE, TeamID.TWO, TeamID.THREE))));
        }
        for (int i = 0; i < 20; i++) {
            gb.moveAndActMotherNature(1);
        }
        for (PawnColour p : PawnColour.values()) {
            for (int i = 0; i < new Random().nextInt(9); i++) {
                gb.addStudentToDiningRoom(p, gb.getMutableTurnOrder().getMutableCurrentPlayer());
            }
        }
        Utils.random(gb.getClouds()).extractContents();
        gb.getMutableTurnOrder().getMutableCurrentPlayer().removeStudentFromEntrance(4);
        System.out.println(GameUI.draw(gb));
    }

}