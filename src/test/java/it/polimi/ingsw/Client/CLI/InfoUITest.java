package it.polimi.ingsw.Client.CLI;


import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

public class InfoUITest {

    @Test
    public void shouldDrawInfos() {
        GameBoard gb = new GameBoard(GameMode.ADVANCED, "ale", "teo");
        PlayerBoard player = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        System.out.println(InfoUI.draw(gb, player.getNickname()));
        gb.getMutableTurnOrder().stepToNextPlayer();
        gb.addCoinToReserve(1);
        System.out.println(InfoUI.draw(gb, player.getNickname()));
    }

}