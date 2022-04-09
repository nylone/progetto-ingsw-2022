package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;

public class ChooseCloudTile extends PlayerAction {

    private final int selectedTile;

    public ChooseCloudTile(int playerBoardId, int selectedTile) {
        super(playerBoardId);
        this.selectedTile = selectedTile;
    }

    public void executeAction(GameBoard ctx) {

    }
}
