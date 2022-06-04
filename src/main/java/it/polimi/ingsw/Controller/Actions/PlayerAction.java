package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public abstract class PlayerAction implements Serializable {
    @Serial
    private static final long serialVersionUID = 200L; // convention: 2 for controller, (01 -> 99) for objects

    private final int playerBoardID;

    public PlayerAction(int playerBoardID) {
        this.playerBoardID = playerBoardID;
    }

    public boolean validate(List<PlayerAction> history, Model ctx) throws InputValidationException {
        return isGameRunning(ctx) && isCorrectTurn(ctx) && isNotDuplicateAction(history);
    }

    final boolean isGameRunning(Model ctx) throws InputValidationException {
        if (ctx.isGameOver()) {
            throw new GenericInputValidationException("GameHandler", "Game is over, action cannot be executed");
        }
        return true;
    }

    // can be used in validate to make sure the turn is correct
    final boolean isCorrectTurn(Model ctx) {
        return ctx.getMutableTurnOrder().getMutableCurrentPlayer().getId() == this.getPlayerBoardID();
    }

    // can be used in validate to make sure the action is not yet in the history
    final boolean isNotDuplicateAction(List<PlayerAction> history) {
        return history.stream().noneMatch(h -> h.getClass() == this.getClass());
    }

    final public int getPlayerBoardID() {
        return playerBoardID;
    }

    public abstract void unsafeExecute(Model ctx) throws Exception;
}
