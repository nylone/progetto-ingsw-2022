package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

public class EndTurnOfActionPhaseTest {

    @Test
    public void testExecute(){
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        

    }
    @Test(expected = InputValidationException.class)
    public void LessThan3MoveStudentException() throws Exception{
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        EndTurnOfActionPhase endPhase = new EndTurnOfActionPhase(currentPlayer.getId());
        endPhase.safeExecute(gh.getHistory(),gameBoard);
    }

    @Test(expected = InputValidationException.class)
    public void OutOfTurnAction() throws Exception{
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        EndTurnOfActionPhase endPhase = new EndTurnOfActionPhase(new PlayerBoard(3,2,"thief", gameBoard.getMutableStudentBag()).getId());
        endPhase.safeExecute(gh.getHistory(),gameBoard);
    }
}
