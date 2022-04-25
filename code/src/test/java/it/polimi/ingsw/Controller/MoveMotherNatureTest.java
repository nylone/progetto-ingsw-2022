package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.IslandGroup;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

import java.util.List;
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
    public void playerCanMoveAccordingToChosenCard() {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE);
        GameBoard gameBoard = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(List.of(player.getMutableAssistantCards()));
        gameBoard.getMutableTurnOrder().setSelectedCard(player, card, player.getMutableAssistantCards());
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

    @Test
    public void playerCantMoveMoreThanAllowed() {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE);
        GameBoard gameBoard = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(List.of(player.getMutableAssistantCards()));
        gameBoard.getMutableTurnOrder().setSelectedCard(player, card, player.getMutableAssistantCards());
        int invalidMovement = card.getMaxMovement() + 1;
        IslandGroup initialPosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        PlayerAction action = new MoveMotherNature(player.getId(), invalidMovement);
        // act
        action.safeExecute(gh.getHistory(), gameBoard);
        // assert
        assertEquals(initialPosition, gameBoard.getMutableIslandField().getMutableMotherNaturePosition());
    }
}