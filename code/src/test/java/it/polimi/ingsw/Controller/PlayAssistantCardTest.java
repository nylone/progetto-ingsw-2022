/*package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PlayAssistantCardTest {

    @Test
    public void cardShouldBeAssociatedToPlayer() {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE);
        GameBoard gameBoard = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 3);
        // act
        action.safeExecute(gh.getHistory(), gameBoard);
        // assert
        assertTrue(player.getMutableAssistantCards()[2].getUsed());
        for (int i = 0; i < player.getMutableAssistantCards().length; i++) {
            if (i != 2) assertTrue(!player.getMutableAssistantCards()[i].getUsed());
        }
        assertTrue(gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getPriority() == 3);
    }
}
*/
