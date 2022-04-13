package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.CharacterCard;
import it.polimi.ingsw.Model.CharacterCardInput;

import java.util.List;

public class PlayCharacterCard extends PlayerAction {

    private final int selectedCard;

    public PlayCharacterCard(int playerBoardId, int selectedCard) {
        super(playerBoardId);
        this.selectedCard = selectedCard;
    }

    @Override
    protected void execute(GameBoard ctx) {
        //ctx.getCharacterCards().get(this.selectedCard).Use();
    }

   /* @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) {

    }*/
}
