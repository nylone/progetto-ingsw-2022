package it.polimi.ingsw.Client.CLI.Components;

import it.polimi.ingsw.Client.CLI.IslandUI;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Card05;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.TeamID;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.IslandGroup;
import it.polimi.ingsw.Model.NoEntryTile;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class IslandUITest {

    @Test
    public void shouldDrawIsland() {
        GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "ale", "teo");

        for (IslandGroup ig : gb.getMutableIslandField().getMutableGroups()) {
            for (int i = 0; i < new Random().nextInt(15); i++) {
                ig.getMutableIslands().get(0).addStudent(Utils.random(List.of(PawnColour.values())));
            }
            ig.swapTower(gb.getTeamMapper().getMutableTowerStorage(Utils.random(List.of(TeamID.values()))));
            ig.addNoEntry(new NoEntryTile(new Card05(gb)));
            System.out.println(IslandUI.draw(ig, gb));
        }
    }

    @Test
    public void shouldDrawIslandGroup() {
        GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "ale", "teo");

        for (IslandGroup ig : gb.getMutableIslandField().getMutableGroups()) {
            for (int i = 0; i < new Random().nextInt(15); i++) {
                ig.getMutableIslands().get(0).addStudent(Utils.random(List.of(PawnColour.values())));
            }
            ig.swapTower(gb.getTeamMapper().getMutableTowerStorage(Utils.random(List.of(TeamID.values()))));
        }
        for (int i = 0; i < 20; i++) {
            gb.moveAndActMotherNature(1);
        }
        for (IslandGroup ig : gb.getMutableIslandField().getMutableGroups()) {
            System.out.println(IslandUI.draw(ig, gb));
        }
    }
}