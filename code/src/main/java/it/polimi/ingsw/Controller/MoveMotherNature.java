package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

import java.util.List;
import java.util.Optional;

public class MoveMotherNature extends PlayerAction {

    private final int distanceToMove;

    public MoveMotherNature(int playerBoardId, int distanceToMove) {
        super(playerBoardId);
        this.distanceToMove = distanceToMove;
    }

    @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) {
        PlayerBoard currentPlayer = ctx.getTurnOrder().getCurrentPlayer();
        Optional<AssistantCard> optionalAssistantCard = ctx.getTurnOrder()
                .getSelectedCard(currentPlayer);

        return super.validate(history, ctx) &&
                optionalAssistantCard.isPresent() &&
                distanceToMove >= 1 &&
                distanceToMove <= optionalAssistantCard.get().getMaxMovement();
    }

    @Override
    protected void unsafeExecute(GameBoard ctx) {
        ctx.moveAndActMotherNature(distanceToMove);
    }
}
