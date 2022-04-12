package it.polimi.ingsw.Controller;

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

    protected abstract void execute(GameBoard ctx);

    // vali
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) {
        return isCorrectTurn(ctx) && isNotDuplicateAction(history);
    }

    // can be used in validate to make sure the turn is correct
    boolean isCorrectTurn(GameBoard ctx) {
        return ctx.getTurnOrder().getCurrent().getId() == this.getPlayerBoardId();
    }

    // can be used in validate to make sure the action is not yet in the history
    boolean isNotDuplicateAction(List<PlayerAction> history) {
        return history.stream().noneMatch(h -> h.getClass() == this.getClass());
    }
}
