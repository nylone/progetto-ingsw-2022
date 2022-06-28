package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.MoveMotherNature;
import it.polimi.ingsw.Controller.Actions.MoveStudent;
import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Controller.Actions.PlayerAction;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.SerializableOptional;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MoveMotherNatureTest {

    Model model = new Model(GameMode.ADVANCED, "ale", "teo");
    Controller gh = new Controller(new ModelWrapper(model, SerializableOptional.empty()), new ArrayList<>());

    @Test
    public void motherNatureShouldBeMoved() throws Exception {
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
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        // move 3 pawns
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }

        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        IslandGroup initialPosition = model.getMutableIslandField().getMutableMotherNaturePosition();
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        List<IslandGroup> groups = model.getMutableIslandField().getMutableGroups();
        gh.executeAction(action);
        // assert
        assertEquals(Utils.modularSelection(initialPosition, groups, randomMovement).getId(),
                model.getMutableIslandField().getMutableMotherNaturePosition().getId());
    }


    @Test
    public void playerCantMoveMoreThanAllowed() throws Exception {
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
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        int invalidMovement = model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement() + 1;
        PlayerAction action = new MoveMotherNature(player.getId(), invalidMovement);
        // act
        try {
            gh.executeAction(action);
        } catch (InvalidElementException exception) {
            assertEquals("An error occurred while validating: DistanceToMove\n" +
                    "The error was: element DistanceToMove was found to be invalid (eg: null, out of bounds or otherwise incorrect).", exception.getMessage());
        }
    }


    @Test(expected = InputValidationException.class)
    public void NoAssistantCardException() throws Exception {
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

        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        gh.executeAction(action);
    }

    @Test
    public void exceedingMovementExceptionWithCard4Active() throws Exception {
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
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        Card04 card04 = new Card04(model);
        CharacterCardInput input = new CharacterCardInput(player);
        if (card04.checkInput(input)) card04.unsafeUseCard(input);
        int invalidMovement = model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement() + 3;
        PlayerAction action = new MoveMotherNature(player.getId(), invalidMovement);

        // place 3 pawns
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }

        try {
            gh.executeAction(action);
        } catch (InvalidElementException exception) {
            assertEquals("An error occurred while validating: DistanceToMove\n" +
                    "The error was: element DistanceToMove was found to be invalid (eg: null, out of bounds or otherwise incorrect).", exception.getMessage());
        }
    }

    @Test
    public void MoveMotherNatureWithoutPlacingEnoughPawns() throws Exception {
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

        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        Card04 card04 = new Card04(model);
        CharacterCardInput input = new CharacterCardInput(player);
        if (card04.checkInput(input)) card04.unsafeUseCard(input);
        int invalidMovement = model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement() + 3;
        PlayerAction action = new MoveMotherNature(player.getId(), invalidMovement);

        // place 3 pawns
        for (int i = 0; i < 2; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);

            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }

        try {
            gh.executeAction(action);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: History\n" +
                    "The error was: MotherNature can't be moved before having placed all 3 pawns", exception.getMessage());
        }
    }

    @Test
    public void DuplicateActionException() throws Exception {
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

        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        // move 3 pawns
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);

            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }

        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        gh.executeAction(action);
        try {
            gh.executeAction(action);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: MoveMotherNature\n" +
                    "The error was: Too many similar actions have been executed", exception.getMessage());
        }
    }
}