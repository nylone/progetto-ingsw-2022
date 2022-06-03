package it.polimi.ingsw.Client.CLI;


import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

public class InfoUITest {

    @Test
    public void shouldDrawInfos() {
        Model gb = new Model(GameMode.ADVANCED, "ale", "teo");
        PlayerBoard player = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        System.out.println(InfoUI.draw(gb, player.getNickname()));
        gb.getMutableTurnOrder().stepToNextPlayer();
        gb.addCoinToReserve(1);
        System.out.println(InfoUI.draw(gb, player.getNickname()));
    }

}