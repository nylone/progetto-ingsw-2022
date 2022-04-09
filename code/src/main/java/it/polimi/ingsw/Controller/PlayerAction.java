package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;

public abstract class PlayerAction {

    private final int playerBoardId;

    public PlayerAction(int playerBoardId) {
        this.playerBoardId = playerBoardId;
    }

    public int getPlayerBoardId() {
        return playerBoardId;
    }

    public abstract void executeAction(GameBoard ctx);

}
