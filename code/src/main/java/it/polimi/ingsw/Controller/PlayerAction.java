package it.polimi.ingsw.Controller;

public abstract class PlayerAction {

    private final int playerBoardId;

    public PlayerAction(int playerBoardId) {
        this.playerBoardId = playerBoardId;
    }

    public int getPlayerBoardId() {
        return playerBoardId;
    }

}
