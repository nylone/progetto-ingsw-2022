package it.polimi.ingsw.Client.CLI.Components;

import it.polimi.ingsw.Client.CLI.PlayerBoardUI;
import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

import java.util.Random;

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

    @Test
    public void shouldDrawPlayerBoard() throws FullContainerException {
        GameBoard gb = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        for (PawnColour p : PawnColour.values()) {
            for (int i = 0; i < new Random().nextInt(9); i++) {
                gb.addStudentToDiningRoom(p, gb.getMutableTurnOrder().getMutableCurrentPlayer());
            }
        }
        System.out.println(PlayerBoardUI.drawPlayerBoard(gb.getMutableTurnOrder().getMutableCurrentPlayer(), gb));
    }
}