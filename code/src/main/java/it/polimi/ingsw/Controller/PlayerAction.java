package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.GameBoard;

import java.util.List;

public abstract class PlayerAction {

    private final int playerBoardId;

    public PlayerAction(int playerBoardId) {
        this.playerBoardId = playerBoardId;
    }

    public int getPlayerBoardId() {
        return playerBoardId;
    }

    protected void safeExecute(List<PlayerAction> history, GameBoard ctx) throws InputValidationException {
        if (validate(history, ctx)) {
            try {
                unsafeExecute(ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void unsafeExecute(GameBoard ctx) throws Exception;

    protected boolean validate(List<PlayerAction> history, GameBoard ctx) throws InputValidationException {
        return isCorrectTurn(ctx) && isNotDuplicateAction(history);
    }

    // can be used in validate to make sure the turn is correct
    boolean isCorrectTurn(GameBoard ctx) {
        return ctx.getTurnOrder().getCurrentPlayer().getId() == this.getPlayerBoardId();
    }

    // can be used in validate to make sure the action is not yet in the history
    boolean isNotDuplicateAction(List<PlayerAction> history) {
        return history.stream().noneMatch(h -> h.getClass() == this.getClass());
    }

    int countDuplicateActions(List<PlayerAction> history) {
        int count = 0;
        for (PlayerAction previousAction : history) {
            if (previousAction.getClass() == this.getClass()) count++;
        }
        return count;
    }
}
