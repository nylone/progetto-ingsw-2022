package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MoveMotherNatureTest {

    @Test
    public void motherNatureShouldBeMoved() {
        // arrange
        GameBoard gameBoard = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        int randomMovement = new Random().nextInt(1, 7);
        IslandGroup initialPosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        MoveMotherNature action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        action.unsafeExecute(gameBoard);
        // assert
        assertEquals(Utils.modularSelection(initialPosition, gameBoard.getMutableIslandField().getMutableGroups(), randomMovement),
                gameBoard.getMutableIslandField().getMutableMotherNaturePosition());
    }

    @Test
    public void playerCanMoveAccordingToChosenCard() throws Exception {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        gameBoard.getMutableTurnOrder().setSelectedCard(player, card);
        int randomMovement = new Random().nextInt(card.getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        IslandGroup initialPosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        action.safeExecute(gh.getHistory(), gameBoard);
        // assert
        assertEquals(Utils.modularSelection(initialPosition, gameBoard.getMutableIslandField().getMutableGroups(), randomMovement),
                gameBoard.getMutableIslandField().getMutableMotherNaturePosition());
    }

    @Test(expected = InputValidationException.class)
    public void playerCantMoveMoreThanAllowed() throws Exception {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random((player.getMutableAssistantCards()));
        gameBoard.getMutableTurnOrder().setSelectedCard(player, card);
        int invalidMovement = card.getMaxMovement() + 1;
        IslandGroup initialPosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        PlayerAction action = new MoveMotherNature(player.getId(), invalidMovement);
        // act
        action.safeExecute(gh.getHistory(), gameBoard);
    }

    @Test(expected = InputValidationException.class)
    public void duplicateMoveMotherNatureException() throws Exception {
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        gameBoard.getMutableTurnOrder().setSelectedCard(player, card);
        int randomMovement = new Random().nextInt(card.getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        System.out.println(gh.getHistory());
        action.safeExecute(gh.getHistory(), gameBoard);
        System.out.println(gh.getHistory());
        action.safeExecute(gh.getHistory(), gameBoard); //exception, duplicate action
    }

    @Test(expected = InputValidationException.class)
    public void NoAssistantCardException() throws Exception {
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        int randomMovement = new Random().nextInt(card.getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        action.safeExecute(gh.getHistory(), gameBoard);
    }

    @Test(expected = InputValidationException.class)
    public void exceedingMovementExceptionWithCard4Active() throws Exception {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        Card04 card04 = new Card04(gameBoard);
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        CharacterCardInput input = new CharacterCardInput(player);
        if (card04.checkInput(input)) card04.unsafeUseCard(input);
        AssistantCard card = Utils.random((player.getMutableAssistantCards()));
        gameBoard.getMutableTurnOrder().setSelectedCard(player, card);
        int invalidMovement = card.getMaxMovement() + 3;
        IslandGroup initialPosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        PlayerAction action = new MoveMotherNature(player.getId(), invalidMovement);
        // act
        action.safeExecute(gh.getHistory(), gameBoard);
    }
}