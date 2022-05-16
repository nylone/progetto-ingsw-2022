package it.polimi.ingsw.Client.CLI.Components;

import it.polimi.ingsw.Client.CLI.PlayerBoardUI;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import org.junit.Test;

public class PlayerBoardUITest {

    @Test
    public void shouldDrawEntrance() {
        GameBoard gb = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        System.out.println(PlayerBoardUI.drawEntrance(gb.getMutableTurnOrder().getMutableCurrentPlayer(), gb));
    }

    @Test
    public void shouldDrawTowers() {
        GameBoard gb = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        System.out.println(PlayerBoardUI.drawTowers(gb.getMutableTurnOrder().getMutableCurrentPlayer(), gb));
    }
}