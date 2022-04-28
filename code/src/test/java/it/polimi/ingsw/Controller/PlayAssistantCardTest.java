package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PlayAssistantCardTest {

    @Test
    public void cardShouldBeAssociatedToPlayer() throws Exception {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 3);
        // act
        gh.executeAction(action);
        // assert
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertTrue(player.getMutableAssistantCards().get(2).getUsed());
        for (int i = 0; i < player.getMutableAssistantCards().size(); i++) {
            if (i != 2) assertTrue(!player.getMutableAssistantCards().get(i).getUsed());
        }
        assertTrue(gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getPriority() == 3);
    }

    @Test(expected = InputValidationException.class)
    public void SelectedAlreadyUsedCardException() throws Exception {
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 3);
        // act
        gh.executeAction(action);

        gh.executeAction(action);
    }

    @Test(expected = InputValidationException.class)
    public void OutOfTurnAccessException() throws Exception {
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayAssistantCard action = new PlayAssistantCard(3, 3);
        // act
        gh.executeAction(action);
    }

    @Test(expected = InputValidationException.class)
    public void AssistantCardIndexOutOfBound() throws Exception {
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayAssistantCard action = new PlayAssistantCard(3, 13);
        // act
        gh.executeAction(action);
    }
}
