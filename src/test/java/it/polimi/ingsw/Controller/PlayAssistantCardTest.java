package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.SerializableOptional;
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
    Controller gh = new Controller(new ModelWrapper(model, SerializableOptional.empty()), new ArrayList<>());

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
                    "The error was: Assitant Card has already been selected by another player", exception.getMessage());
        }


    }

    @Test
    public void PlayerBoardIndexOutOfBoundException() throws Exception {
        PlayAssistantCard action = new PlayAssistantCard(3, 3);
        // act
        try {
            gh.executeAction(action);
        } catch (InvalidElementException exception) {
            assertEquals("An error occurred while validating: PlayerBoardID out of range\n" +
                    "The error was: element PlayerBoardID out of range was found to be invalid (eg: null, out of bounds or otherwise incorrect).", exception.getMessage());
        }
    }

    @Test
    public void AssistantCardIndexOutOfBound() throws Exception {
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 13);
        // act
        try {
            gh.executeAction(action);
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
        gh.executeAction(playAssistantCard);
        //second player assistantcard action
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        card = player.getMutableAssistantCards().get(9);
        PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard1);

        //repeated assistantCard action not valid
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        card = player.getMutableAssistantCards().get(1);
        playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        try {
            gh.executeAction(playAssistantCard);
        } catch (GenericInputValidationException e) {
            assertEquals("An error occurred while validating: Assitant Card\n" +
                    "The error was: Assitant Card may only be used during the setup phase", e.getMessage());
        }
    }
}
