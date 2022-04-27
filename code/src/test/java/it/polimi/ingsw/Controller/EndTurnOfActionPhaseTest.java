package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

public class EndTurnOfActionPhaseTest {

    @Test
    public void testExecute() throws Exception {
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        MoveDestination moveIsland = MoveDestination.toIsland(0);
        MoveDestination moveEntrance = MoveDestination.toDiningRoom();
        MoveStudent firstMove = new MoveStudent(currentPlayer.getId(), 0, moveIsland);
        MoveStudent secondMove = new MoveStudent(currentPlayer.getId(), 0, moveEntrance);
        MoveStudent thirdMove = firstMove;

        firstMove.safeExecute(gh.getHistory(), gameBoard);
        secondMove.safeExecute(gh.getHistory(), gameBoard);
        thirdMove.safeExecute(gh.getHistory(), gameBoard);

        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(currentPlayer.getId(), 0);
        chooseCloudTile.safeExecute(gh.getHistory(), gameBoard);

        EndTurnOfActionPhase endTurnOfActionPhase = new EndTurnOfActionPhase(currentPlayer.getId());
        endTurnOfActionPhase.safeExecute(gh.getHistory(), gameBoard);

    }

    @Test(expected = InputValidationException.class)
    public void LessThan3MoveStudentException() throws Exception {
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        EndTurnOfActionPhase endPhase = new EndTurnOfActionPhase(currentPlayer.getId());
        endPhase.safeExecute(gh.getHistory(), gameBoard);
    }

    @Test//(expected = GenericInputValidationException.class)
    public void OutOfTurnAction() throws Exception {
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        MoveDestination moveIsland = MoveDestination.toIsland(0);
        MoveDestination moveEntrance = MoveDestination.toDiningRoom();
        MoveStudent firstMove = new MoveStudent(currentPlayer.getId(), 0, moveIsland);
        MoveStudent secondMove = new MoveStudent(currentPlayer.getId(), 0, moveEntrance);
        MoveStudent thirdMove = firstMove;

        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(currentPlayer.getId(), 0);
        chooseCloudTile.safeExecute(gh.getHistory(), gameBoard);

        firstMove.safeExecute(gh.getHistory(), gameBoard);
        secondMove.safeExecute(gh.getHistory(), gameBoard);
        thirdMove.safeExecute(gh.getHistory(), gameBoard);


        EndTurnOfActionPhase endPhase = new EndTurnOfActionPhase(new PlayerBoard(3, 2, "thief", gameBoard.getMutableStudentBag()).getId());
        endPhase.safeExecute(gh.getHistory(), gameBoard);
    }
}
