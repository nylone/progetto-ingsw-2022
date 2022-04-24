package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.CharacterCard;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

import java.util.List;

public class PlayCharacterCard extends PlayerAction {

    private final int selectedCard;

    public PlayCharacterCard(int playerBoardId, int selectedCard) {
        super(playerBoardId);
        this.selectedCard = selectedCard;
    }

    @Override
    protected void unsafeExecute(GameBoard ctx) {
        PlayerBoard caller = ctx.getTurnOrder().getCurrentPlayer();
        CharacterCard characterCard = ctx.getCharacterCards().get(this.selectedCard);
        caller.PayCharacterEffect(characterCard.getCost());
        if (characterCard.getTimeUsed() > 0) {
            ctx.addToCoinReserve(characterCard.getCost());
        } else {
            ctx.addToCoinReserve(characterCard.getCost() - 1); //the first time, one coin has to be placed on the card and not in the coin reserve
        }
        //characterCard.Use(); todo come viene passato il CharacterCardInput alla carta?
    }

    protected boolean validate(List<PlayerAction> history, GameBoard ctx) {
        PlayerBoard caller = ctx.getTurnOrder().getCurrentPlayer();
        CharacterCard selectedCard = ctx.getCharacterCards().get(this.selectedCard);

        return super.validate(history, ctx) &&
                this.selectedCard >= 0 && this.selectedCard < 3 &&
                caller.getCoinBalance() >= selectedCard.getCost();
    }
}
