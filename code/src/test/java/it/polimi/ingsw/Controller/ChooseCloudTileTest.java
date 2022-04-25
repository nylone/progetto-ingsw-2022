package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChooseCloudTileTest {

    @Test
    public void playerCanTakeStudentsFromCloud() {
        // arrange
        GameBoard gameBoard = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        int selectedCloud = Utils.random(gameBoard.getClouds()).getId();
        ChooseCloudTile action = new ChooseCloudTile(currentPlayer.getId(), selectedCloud);
        currentPlayer.getEntranceStudents().remove(6);
        currentPlayer.getEntranceStudents().remove(5);
        currentPlayer.getEntranceStudents().remove(4);
        // act
        action.unsafeExecute(gameBoard);
        // assert
        assertTrue(gameBoard.getClouds().get(selectedCloud).getContents().size() == 0);
        assertTrue(currentPlayer.getEntranceStudents().size() == 7);
    }

    @Test
    public void fullEntranceShouldPreventActionExecution() {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE);
        GameBoard gameBoard = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        int selectedCloud = Utils.random(gameBoard.getClouds()).getId();
        ChooseCloudTile action = new ChooseCloudTile(currentPlayer.getId(), selectedCloud);
        // act
        action.safeExecute(gh.getHistory(), gameBoard);
        // assert
        assertTrue(gameBoard.getClouds().get(selectedCloud).getContents().size() == 3);
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