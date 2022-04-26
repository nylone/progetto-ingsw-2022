package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChooseCloudTileTest {

    @Test
    public void playerCanTakeStudentsFromCloud() throws Exception {
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
        action.safeExecute(gh.getHistory(), gameBoard);
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

    @Test(expected = InputValidationException.class)
    public void EntranceFullException() throws Exception{
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        ChooseCloudTile action = new ChooseCloudTile(currentPlayer.getId(), 0);
        currentPlayer.removeStudentFromEntrance(6);
        // act
        action.safeExecute(gh.getHistory(),gameBoard);
        // assert
    }

    @Test(expected = GenericInputValidationException.class)
    public void duplicateChooseCloudTileException() throws Exception{
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        int selectedCloud = gameBoard.getClouds().get(0).getId();
        ChooseCloudTile action = new ChooseCloudTile(currentPlayer.getId(), selectedCloud);
        currentPlayer.removeStudentFromEntrance(6);
        currentPlayer.removeStudentFromEntrance(5);
        currentPlayer.removeStudentFromEntrance(4);
        // act
        action.safeExecute(gh.getHistory(), gameBoard);

        currentPlayer.removeStudentFromEntrance(6);
        currentPlayer.removeStudentFromEntrance(5);
        currentPlayer.removeStudentFromEntrance(4);

        selectedCloud = gameBoard.getClouds().get(1).getId();
        action = new ChooseCloudTile(currentPlayer.getId(), selectedCloud);

        action.safeExecute(gh.getHistory(), gameBoard); //exception
    }

    @Test(expected = InputValidationException.class)
    public void CloudIndexOutOfBound() throws Exception{
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        ChooseCloudTile action = new ChooseCloudTile(currentPlayer.getId(), 10);
        currentPlayer.removeStudentFromEntrance(6);
        currentPlayer.removeStudentFromEntrance(5);
        currentPlayer.removeStudentFromEntrance(4);
        // act
        action.safeExecute(gh.getHistory(), gameBoard);
    }

    @Test(expected = InputValidationException.class)
    public void CloudEmptyException() throws Exception{
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        ChooseCloudTile action = new ChooseCloudTile(currentPlayer.getId(), 0);
        currentPlayer.removeStudentFromEntrance(6);
        currentPlayer.removeStudentFromEntrance(5);
        currentPlayer.removeStudentFromEntrance(4);
        // act
        action.safeExecute(gh.getHistory(), gameBoard);

        currentPlayer.removeStudentFromEntrance(6);
        currentPlayer.removeStudentFromEntrance(5);
        currentPlayer.removeStudentFromEntrance(4);
        // act
        action.safeExecute(gh.getHistory(), gameBoard);
    }
}