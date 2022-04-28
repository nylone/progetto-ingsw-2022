package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MoveStudentTest {

    GameHandler gh = new GameHandler(GameMode.ADVANCED, "ale", "teo");
    GameBoard gameBoard = gh.getModelCopy();
    PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();

    @Test
    public void testSingleMovementToIsland() throws Exception{
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 3);
        gh.executeAction(action); //play the assistant card
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        MoveDestination moveDestination = MoveDestination.toIsland(0);
        PawnColour toMove = player.getEntranceStudents().get(1).get();
        int previousCount = (int) gameBoard.getMutableIslandField().getMutableIslandById(0).getStudents()
                .stream().filter(pawnColour -> toMove == pawnColour).count();
        MoveStudent moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        gh.executeAction(moveStudent);
        gameBoard = gh.getModelCopy();
        int updatedCount = (int) gameBoard.getMutableIslandField().getMutableIslandById(0).getStudents()
                .stream().filter(pawnColour -> toMove == pawnColour).count();
        assertTrue(updatedCount == previousCount + 1);
    }

    @Test
    public void testSingleMovementToDiningRoom() throws Exception{
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 3);
        gh.executeAction(action); //play the assistant card
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        MoveDestination destination = MoveDestination.toDiningRoom();
        PawnColour toMove = player.getEntranceStudents().get(0).get();
        int previousCount = player.getDiningRoomCount(toMove);
        MoveStudent moveStudent = new MoveStudent(player.getId(), 0, destination);
        gh.executeAction(moveStudent);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertTrue(player.getDiningRoomCount(toMove) == previousCount + 1);
    }

    @Test
    public void Max3MovementsWithoutCharacterCardTest() throws Exception{
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 3);
        gh.executeAction(action); //play the assistant card
        gameBoard = gh.getModelCopy();
        MoveDestination destination = MoveDestination.toDiningRoom();
        MoveStudent moveStudent = new MoveStudent(player.getId(), 0, destination);
        gh.executeAction(moveStudent);
        gameBoard = gh.getModelCopy();
        destination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 1, destination);
        gh.executeAction(moveStudent);
        gameBoard = gh.getModelCopy();
        destination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 2, destination);
        gh.executeAction(moveStudent);
        gameBoard = gh.getModelCopy();
        destination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 3 , destination);
        try {
            gh.executeAction(moveStudent); //exception
        }catch (InputValidationException exception){
            assertEquals("An error occurred while validating: History\n" +
                    "The error was: only 3 pawns can be moved from entrance without playing CharacterCards", exception.getMessage());
        }

    }

    @Test
    public void testMovementWithoutAssistantCard() throws Exception{
        MoveDestination moveDestination = MoveDestination.toIsland(0);
        MoveStudent moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        try {
            gh.executeAction(moveStudent);
        }catch (InputValidationException exception) {
            assertEquals("An error occurred while validating: History\n" +
                    "The error was: No PlayAssistantCard has been played", exception.getMessage());
        }
    }

    @Test(expected = InvalidElementException.class)
    public void InvalidPlayerBoardIndexException() throws Exception{
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 3);
        gh.executeAction(action); //play the assistant card
        gameBoard = gh.getModelCopy();
        MoveDestination destination = MoveDestination.toDiningRoom();
        MoveStudent moveStudent = new MoveStudent(3, 0, destination);
        gh.executeAction(moveStudent);
    }

    @Test(expected = InvalidElementException.class)
    public void InvalidEntranceIndexException() throws Exception{
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 3);
        gh.executeAction(action); //play the assistant card
        gameBoard = gh.getModelCopy();
        MoveDestination destination = MoveDestination.toDiningRoom();
        MoveStudent moveStudent = new MoveStudent(1, 12, destination);
        gh.executeAction(moveStudent);
    }

    @Test(expected = InvalidElementException.class)
    public void InvalidIslandIdException() throws Exception{
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 3);
        gh.executeAction(action); //play the assistant card
        gameBoard = gh.getModelCopy();
        MoveDestination moveDestination = MoveDestination.toIsland(15);
        MoveStudent moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        gh.executeAction(moveStudent);
    }

    @Test(expected = InvalidElementException.class)
    public void SelectedEntrancePositionEmpty() throws Exception{
        PlayAssistantCard action = new PlayAssistantCard(player.getId(), 3);
        gh.executeAction(action); //play the assistant card
        gameBoard = gh.getModelCopy();
        MoveDestination destination = MoveDestination.toDiningRoom();
        MoveStudent moveStudent = new MoveStudent(player.getId(), 0, destination);
        gh.executeAction(moveStudent);
        gameBoard = gh.getModelCopy();

        moveStudent = new MoveStudent(player.getId(), 0, destination);
        gh.executeAction(moveStudent);

    }


}
