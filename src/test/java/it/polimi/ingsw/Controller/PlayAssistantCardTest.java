package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.ModelWrapper;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayAssistantCardTest {

    Model model = new Model(GameMode.ADVANCED, "ale", "teo");
    Controller gh = new Controller(new ModelWrapper(model, null), new ArrayList<>());

    @Test
    public void cardShouldBeAssociatedToPlayer() throws Exception {
        // arrange
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();

        while (true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;

            if (model.getMutableTurnOrder().getSelectedCards().stream()
                    .noneMatch(selected -> selected.getPriority() == finalCard.getPriority())) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }
        // assert
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        assertTrue(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getUsed());
        for (int i = 1; i <= player.getMutableAssistantCards().size(); i++) {
            if (i != model.getMutableTurnOrder().getMutableSelectedCard(player).get().getPriority())
                assertFalse(player.getMutableAssistantCards().get(i - 1).getUsed());
        }
        assertEquals(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getPriority(), player.getMutableAssistantCards().stream().filter(AssistantCard::getUsed).findFirst().get().getPriority());
    }

    @Test(expected = InputValidationException.class)
    public void SelectedAlreadyUsedCardException() throws Exception {
        Controller gh = new Controller(GameMode.SIMPLE, null, "ale", "teo");
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 3);
        // act
        gh.executeAction(action);
        gh.executeAction(action);
    }

    @Test
    public void SelectedSamePriorityCardException() throws InputValidationException {
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 3);
        // act
        gh.executeAction(action);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        action = new PlayAssistantCard(player.getId(), 3);

        try {
            gh.executeAction(action);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: Assitant Card\n" +
                    "The error was: Assitant Card has an already selected priority", exception.getMessage());
        }


    }

    @Test(expected = InputValidationException.class)
    public void OutOfTurnAccessException() throws Exception {
        PlayAssistantCard action = new PlayAssistantCard(3, 3);
        // act
        gh.executeAction(action);
    }

    @Test(expected = InputValidationException.class)
    public void AssistantCardIndexOutOfBound() throws Exception {
        PlayAssistantCard action = new PlayAssistantCard(3, 13);
        // act
        gh.executeAction(action);
    }
}
