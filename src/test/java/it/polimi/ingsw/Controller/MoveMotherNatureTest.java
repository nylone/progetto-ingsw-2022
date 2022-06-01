package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.MoveMotherNature;
import it.polimi.ingsw.Controller.Actions.MoveStudent;
import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Controller.Actions.PlayerAction;
import it.polimi.ingsw.Controller.Enums.MoveDestination;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MoveMotherNatureTest {

    GameBoard gameBoard = new GameBoard(GameMode.ADVANCED, "ale", "teo");
    GameHandler gh = new GameHandler(gameBoard, new ArrayList<>(6));
    @Test
    public void motherNatureShouldBeMoved() throws Exception {
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        while (true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if (gameBoard.getMutableTurnOrder().getSelectedCards().stream()
                    .noneMatch(selected -> selected.getPriority() == finalCard.getPriority())) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        // move 3 pawns
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        }

        int randomMovement = new Random().nextInt(gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        IslandGroup initialPosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        List<IslandGroup> groups = gameBoard.getMutableIslandField().getMutableGroups();
        gh.executeAction(action);
        // assert
        assertEquals(Utils.modularSelection(initialPosition, groups, randomMovement).getId(),
                gameBoard.getMutableIslandField().getMutableMotherNaturePosition().getId());
    }


    @Test
    public void playerCantMoveMoreThanAllowed() throws Exception {
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        while (true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if (gameBoard.getMutableTurnOrder().getSelectedCards().stream()
                    .noneMatch(selected -> selected.getPriority() == finalCard.getPriority())) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        int invalidMovement = gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement() + 1;
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
    public void duplicateMoveMotherNatureException() throws Exception {
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        gameBoard.getMutableTurnOrder().setSelectedCard(player, card);
        int randomMovement = new Random().nextInt(gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        gh.executeAction(action);
        gh.executeAction(action); //exception, duplicate action
    }

    @Test(expected = InputValidationException.class)
    public void NoAssistantCardException() throws Exception {
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        while (true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if (gameBoard.getMutableTurnOrder().getSelectedCards().stream()
                    .noneMatch(selected -> selected.getPriority() == finalCard.getPriority())) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }

        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        int randomMovement = new Random().nextInt(gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        gh.executeAction(action);
    }

    @Test
    public void exceedingMovementExceptionWithCard4Active() throws Exception {
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        while (true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if (gameBoard.getMutableTurnOrder().getSelectedCards().stream()
                    .noneMatch(selected -> selected.getPriority() == finalCard.getPriority())) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        Card04 card04 = new Card04(gameBoard);
        CharacterCardInput input = new CharacterCardInput(player);
        if (card04.checkInput(input)) card04.unsafeUseCard(input);
        int invalidMovement = gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement() + 3;
        PlayerAction action = new MoveMotherNature(player.getId(), invalidMovement);

        // place 3 pawns
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
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
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        while (true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if (gameBoard.getMutableTurnOrder().getSelectedCards().stream()
                    .noneMatch(selected -> selected.getPriority() == finalCard.getPriority())) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }

        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        Card04 card04 = new Card04(gameBoard);
        CharacterCardInput input = new CharacterCardInput(player);
        if (card04.checkInput(input)) card04.unsafeUseCard(input);
        int invalidMovement = gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement() + 3;
        PlayerAction action = new MoveMotherNature(player.getId(), invalidMovement);

        // place 3 pawns
        for (int i = 0; i < 2; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);

            player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
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
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);

        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        while (true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if (gameBoard.getMutableTurnOrder().getSelectedCards().stream()
                    .noneMatch(selected -> selected.getPriority() == finalCard.getPriority())) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }

        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        // move 3 pawns
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);

            player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        }

        int randomMovement = new Random().nextInt(gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        gh.executeAction(action);
        try {
            gh.executeAction(action);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: Action\n" +
                    "The error was: this action can't be executed more than once or be executed by other player than the current", exception.getMessage());
        }
    }
}