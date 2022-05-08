package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.AssistantCard;
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
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        while(true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if(!(gameBoard.getMutableTurnOrder().getSelectedCards().stream()
                    .anyMatch(selected -> selected.getPriority() == finalCard.getPriority()))) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }
        // assert
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertTrue(gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getUsed());
        for (int i = 1; i <= player.getMutableAssistantCards().size(); i++) {
            if (i != gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getPriority()) assertTrue(!player.getMutableAssistantCards().get(i-1).getUsed());
        }
        assertTrue(gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getPriority() ==
                player.getMutableAssistantCards().stream().filter(assistantCard -> assistantCard.getUsed()).findFirst().get().getPriority());
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