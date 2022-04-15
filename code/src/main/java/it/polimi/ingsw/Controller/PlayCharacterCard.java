package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;

public class PlayCharacterCard extends PlayerAction {

    private final int selectedCard;

    public PlayCharacterCard(int playerBoardId, int selectedCard) {
        super(playerBoardId);
        this.selectedCard = selectedCard;
    }

    @Override
    protected void unsafeExecute(GameBoard ctx) {

    }
}
