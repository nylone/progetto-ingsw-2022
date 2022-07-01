package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.OptionalValue;
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
    //create a shared model that will be used by all tests
    final Model model = new Model(GameMode.ADVANCED, "ale", "teo");

    //create a shared controller that will be used by all tests
    final Controller controller = new Controller(new ModelWrapper(model, OptionalValue.empty()), new ArrayList<>());

    @Test
    public void cardShouldBeAssociatedToPlayer() throws Exception {
        //get current player
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //pick one assistantCard randomly
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        //create and execute playAssistantCard
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        controller.executeAction(playAssistantCard);
        //get current player
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //repeat process for other players adding one check to avoid repeated assistant cards
        while (true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;

            if (model.getMutableTurnOrder().getSelectedCards().stream()
                    .noneMatch(selected -> selected.getPriority() == finalCard.getPriority())) {
                controller.executeAction(playAssistantCard1);
                break;
            }
        }
        // assert
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //verify that player's assistantCard has been used
        assertTrue(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getUsed());
        //check that all other assistant cards has not been used
        for (int i = 1; i <= player.getMutableAssistantCards().size(); i++) {
            if (i != model.getMutableTurnOrder().getMutableSelectedCard(player).get().getPriority())
                assertFalse(player.getMutableAssistantCards().get(i - 1).getUsed());
        }
        assertEquals(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getPriority(), player.getMutableAssistantCards().stream().filter(AssistantCard::getUsed).findFirst().get().getPriority());
    }

    @Test
    public void SelectedSamePriorityCardException() throws InputValidationException {
        //get current player
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //create playAssistantCard action
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 3);
        // act
        controller.executeAction(action);
        //create PlayAssistantCard with same card
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        action = new PlayAssistantCard(player.getId(), 3);
        //try to execute playAssistantCard action (an exception will be thrown)
        try {
            controller.executeAction(action);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: Assitant Card\n" +
                    "The error was: has already been selected by another player", exception.getMessage());
        }


    }

    @Test
    public void PlayerBoardIndexOutOfBoundException() throws Exception {
        //try to play an assistant card with an invalid playerBoard's id
        PlayAssistantCard action = new PlayAssistantCard(3, 3);
        // act
        try {
            controller.executeAction(action);
        } catch (InvalidElementException exception) {
            assertEquals("An error occurred while validating: PlayerBoardID out of range\n" +
                    "The error was: element PlayerBoardID out of range was found to be invalid (eg: null, out of bounds or otherwise incorrect).", exception.getMessage());
        }
    }

    @Test
    public void AssistantCardIndexOutOfBound() throws Exception {
        //try to play an assistant card with an invalid assistantCard's priority
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 13);
        // act
        try {
            controller.executeAction(action);
        } catch (InvalidElementException exception) {
            assertEquals("An error occurred while validating: Assitant Card\n" +
                    "The error was: element Assitant Card was found to be invalid (eg: null, out of bounds or otherwise incorrect).", exception.getMessage());
        }
    }

    @Test
    public void playAssistantCardOutOfSetupPhase() throws Exception {
        //first player assistantCard action
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = player.getMutableAssistantCards().get(0);
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        controller.executeAction(playAssistantCard);
        //second player assistantcard action
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        card = player.getMutableAssistantCards().get(9);
        PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
        controller.executeAction(playAssistantCard1);

        //repeated assistantCard action not valid
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        card = player.getMutableAssistantCards().get(1);
        playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        try {
            controller.executeAction(playAssistantCard);
        } catch (GenericInputValidationException e) {
            assertEquals("An error occurred while validating: Assitant Card\n" +
                    "The error was: may only be used during the setup phase", e.getMessage());
        }
    }
}
