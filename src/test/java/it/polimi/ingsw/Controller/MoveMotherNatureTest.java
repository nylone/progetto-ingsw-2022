package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.MoveMotherNature;
import it.polimi.ingsw.Controller.Actions.MoveStudent;
import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Controller.Actions.PlayerAction;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.OptionalValue;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MoveMotherNatureTest {

    //create a shared model that will be used by all tests
    Model model = new Model(GameMode.ADVANCED, "ale", "teo");
    //create a shared controller that will be used by all tests
    Controller controller = new Controller(new ModelWrapper(model, OptionalValue.empty()), new ArrayList<>());

    @Test
    public void motherNatureShouldBeMoved() throws Exception {
        //----- PLAY ASSISTANT CARD (REFER TO PLAYASSISTANTCARD TEST CLASS FOR FURTHER INFORMATION) ----
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        controller.executeAction(playAssistantCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
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
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        // move 3 student (refer to MoveStudentTest for further information)
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            controller.executeAction(moveStudent);
            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        //get random steps
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        IslandGroup initialPosition = model.getMutableIslandField().getMutableMotherNaturePosition();
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        List<IslandGroup> groups = model.getMutableIslandField().getMutableGroups();
        controller.executeAction(action);
        // assert
        assertEquals(Utils.modularSelection(initialPosition, groups, randomMovement).getId(),
                model.getMutableIslandField().getMutableMotherNaturePosition().getId());
    }


    @Test
    public void playerCantMoveMoreThanAllowed() throws Exception {
        //----- PLAY ASSISTANT CARD (REFER TO PLAYASSISTANTCARD TEST CLASS FOR FURTHER INFORMATION) ----
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        controller.executeAction(playAssistantCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
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
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        // move 3 student (refer to MoveStudentTest for further information)
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            controller.executeAction(moveStudent);
            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        //create a moveMotherNature action with more steps than allowed
        int invalidMovement = model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement() + 1;
        PlayerAction action = new MoveMotherNature(player.getId(), invalidMovement);
        // act
        try {
            controller.executeAction(action);
        } catch (InvalidElementException exception) {
            assertEquals("An error occurred while validating: DistanceToMove\n" +
                    "The error was: element DistanceToMove was found to be invalid (eg: null, out of bounds or otherwise incorrect).", exception.getMessage());
        }
    }
    @Test
    public void exceedingMovementExceptionWithCard4Active() throws Exception {
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //----- PLAY ASSISTANT CARD (REFER TO PLAYASSISTANTCARD TEST CLASS FOR FURTHER INFORMATION) ----
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        controller.executeAction(playAssistantCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
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
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //create and activate card04
        Card04 card04 = new Card04(model);
        CharacterCardInput input = new CharacterCardInput(player);
        if (card04.checkInput(input).isEmpty()) card04.unsafeUseCard(input);
        int invalidMovement = model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement() + 3;
        PlayerAction action = new MoveMotherNature(player.getId(), invalidMovement);

        // move 3 student (refer to MoveStudentTest for further information)
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            controller.executeAction(moveStudent);
            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        //create a moveMotherNature action with more steps than allowed (even with the bonus given by card04)
        try {
            controller.executeAction(action);
        } catch (InvalidElementException exception) {
            assertEquals("An error occurred while validating: DistanceToMove\n" +
                    "The error was: element DistanceToMove was found to be invalid (eg: null, out of bounds or otherwise incorrect).", exception.getMessage());
        }
    }

    @Test
    public void MoveMotherNatureWithoutPlacingEnoughPawns() throws Exception {
        //----- PLAY ASSISTANT CARD (REFER TO PLAYASSISTANTCARD TEST CLASS FOR FURTHER INFORMATION) ----
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        controller.executeAction(playAssistantCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
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
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        // move only 2 student (refer to MoveStudentTest for further information)
        for (int i = 0; i < 2; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            controller.executeAction(moveStudent);
            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        //get random steps
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        //try to move mother nature without moving enough students
        try {
            controller.executeAction(action);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: History\n" +
                    "The error was: MotherNature can't be moved before having placed all 3 pawns", exception.getMessage());
        }
    }

    @Test
    public void DuplicateActionException() throws Exception {
        //----- PLAY ASSISTANT CARD (REFER TO PLAYASSISTANTCARD TEST CLASS FOR FURTHER INFORMATION) ----
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        controller.executeAction(playAssistantCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
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

        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //move 3 student (refer to MoveStudentTest for further information)
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            controller.executeAction(moveStudent);

            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        //create and execute moveMotherNature action
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        controller.executeAction(action);
        //try to move mother nature again
        try {
            controller.executeAction(action);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: MoveMotherNature\n" +
                    "The error was: Too many similar actions have been executed", exception.getMessage());
        }
    }
}