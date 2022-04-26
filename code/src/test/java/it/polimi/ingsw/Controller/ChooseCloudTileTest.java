package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChooseCloudTileTest {

    @Test
    public void playerCanTakeStudentsFromCloud() throws InvalidContainerIndexException {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        int selectedCloud = Utils.random(gameBoard.getClouds()).getId();
        ChooseCloudTile action = new ChooseCloudTile(currentPlayer.getId(), selectedCloud);
        currentPlayer.removeStudentFromEntrance(6);
        currentPlayer.removeStudentFromEntrance(5);
        currentPlayer.removeStudentFromEntrance(4);
        // act
        action.unsafeExecute(gameBoard);
        // assert
        assertTrue(gameBoard.getClouds().get(selectedCloud).getContents().size() == 0);
        assertTrue(currentPlayer.getEntranceStudents().size() == 7);
    }

    @Test
    public void fullEntranceShouldPreventActionExecution() throws InputValidationException, InvalidContainerIndexException {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        int selectedCloud = Utils.random(gameBoard.getClouds()).getId();
        ChooseCloudTile action = new ChooseCloudTile(currentPlayer.getId(), selectedCloud);
        currentPlayer.removeStudentFromEntrance(currentPlayer.getEntranceSize() - 1);
        currentPlayer.removeStudentFromEntrance(currentPlayer.getEntranceSize() - 2);
        currentPlayer.removeStudentFromEntrance(currentPlayer.getEntranceSize() - 3);
        // act
        action.safeExecute(gh.getHistory(), gameBoard);
        // assert
        assertTrue(currentPlayer.getEntranceStudents().size() == 7);
    }

    @Test
    public void fullEntranceShouldLogExceptionInUnsafeEnvironment() {
        // arrange
        GameBoard gameBoard = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        int selectedCloud = Utils.random(gameBoard.getClouds()).getId();
        ChooseCloudTile action = new ChooseCloudTile(currentPlayer.getId(), selectedCloud);
        // act
        action.unsafeExecute(gameBoard);
        // assert
        assertTrue(currentPlayer.getEntranceStudents().size() == 7);
    }
}