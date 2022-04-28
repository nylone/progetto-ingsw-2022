package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MoveMotherNatureTest {

    GameHandler gh = new GameHandler(GameMode.ADVANCED, "ale", "teo");
    GameBoard gameBoard = gh.getModelCopy();
    PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
    PlayAssistantCard action = new PlayAssistantCard(player.getId(), 6);
    @Test
    public void motherNatureShouldBeMoved() throws Exception {
        // arrange
        gh.executeAction(action); //play the assistant card
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        for(int i=0; i<3; i++){
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            gameBoard = gh.getModelCopy();
            player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        int maxMovement = gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement();
        int randomMovement = new Random().nextInt(1, maxMovement);
        IslandGroup initialPosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        MoveMotherNature action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        List<IslandGroup> groups = gameBoard.getMutableIslandField().getMutableGroups();
        gh.executeAction(action);
        gameBoard = gh.getModelCopy();
        // assert
        assertEquals(Utils.modularSelection(initialPosition, groups, randomMovement).getId(),
                gameBoard.getMutableIslandField().getMutableMotherNaturePosition().getId());
    }

    @Test
    public void noAssistantCardSelected() throws Exception{
        
    }
    @Test
    public void playerCanMoveAccordingToChosenCard() throws Exception {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        gameBoard.getMutableTurnOrder().setSelectedCard(player, card);
        int randomMovement = new Random().nextInt(card.getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        IslandGroup initialPosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        gh.executeAction(action);
        // assert
        assertEquals(Utils.modularSelection(initialPosition, gameBoard.getMutableIslandField().getMutableGroups(), randomMovement),
                gameBoard.getMutableIslandField().getMutableMotherNaturePosition());
    }

    @Test(expected = InputValidationException.class)
    public void playerCantMoveMoreThanAllowed() throws Exception {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random((player.getMutableAssistantCards()));
        gameBoard.getMutableTurnOrder().setSelectedCard(player, card);
        int invalidMovement = card.getMaxMovement() + 1;
        IslandGroup initialPosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        PlayerAction action = new MoveMotherNature(player.getId(), invalidMovement);
        // act
        gh.executeAction(action);
    }

    @Test(expected = InputValidationException.class)
    public void duplicateMoveMotherNatureException() throws Exception {
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        gameBoard.getMutableTurnOrder().setSelectedCard(player, card);
        int randomMovement = new Random().nextInt(card.getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        gh.executeAction(action);
        gh.executeAction(action); //exception, duplicate action
    }

    @Test(expected = InputValidationException.class)
    public void NoAssistantCardException() throws Exception {
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        int randomMovement = new Random().nextInt(card.getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        gh.executeAction(action);
    }

    @Test(expected = InputValidationException.class)
    public void exceedingMovementExceptionWithCard4Active() throws Exception {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
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
        gh.executeAction(action);
    }
}