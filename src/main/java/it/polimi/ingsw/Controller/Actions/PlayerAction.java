package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.GameBoard;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public abstract class PlayerAction implements Serializable {
    @Serial
    private static final long serialVersionUID = 200L; // convention: 2 for controller, (01 -> 99) for objects

    private final int playerBoardId;

    public PlayerAction(int playerBoardId) {
        this.playerBoardId = playerBoardId;
    }

    public final void safeExecute(List<PlayerAction> history, GameBoard ctx) throws InputValidationException {
        if (validate(history, ctx)) {
            try {
                unsafeExecute(ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean validate(List<PlayerAction> history, GameBoard ctx) throws InputValidationException {
        return isGameRunning(ctx) && isCorrectTurn(ctx) && isNotDuplicateAction(history);
    }

    public abstract void unsafeExecute(GameBoard ctx) throws Exception;

    boolean isGameRunning(GameBoard ctx) throws InputValidationException {
        if (ctx.isGameOver()) {
            throw new GenericInputValidationException("GameHandler", "Game is over, action cannot be executed");
        }
        return true;
    }

    // can be used in validate to make sure the turn is correct
    boolean isCorrectTurn(GameBoard ctx) {
        return ctx.getMutableTurnOrder().getMutableCurrentPlayer().getId() == this.getPlayerBoardId();
    }

    // can be used in validate to make sure the action is not yet in the history
    boolean isNotDuplicateAction(List<PlayerAction> history) {
        return history.stream().noneMatch(h -> h.getClass() == this.getClass());
    }

    public int getPlayerBoardId() {
        return playerBoardId;
    }

    int countDuplicateActions(List<PlayerAction> history) {
        int count = 0;
        for (PlayerAction previousAction : history) {
            if (previousAction.getClass() == this.getClass()) count++;
        }
        return count;
    }
}
