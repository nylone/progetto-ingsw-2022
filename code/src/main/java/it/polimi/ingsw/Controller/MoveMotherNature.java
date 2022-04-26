package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.Optional;

import java.util.List;

import static it.polimi.ingsw.Constants.INPUT_NAME_ASSISTANT_CARD;

public class MoveMotherNature extends PlayerAction {

    private final int distanceToMove;

    public MoveMotherNature(int playerBoardId, int distanceToMove) {
        super(playerBoardId);
        this.distanceToMove = distanceToMove;
    }

    @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) throws InputValidationException {
        PlayerBoard currentPlayer = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        Optional<AssistantCard> optionalAssistantCard = ctx.getMutableTurnOrder()
                .getMutableSelectedCard(currentPlayer);

        if (!super.validate(history, ctx)) {
            throw new GenericInputValidationException("Action", "this action can't be executed more than once or be executed by other player than the current");
        }
        if (optionalAssistantCard.isEmpty()) {
            throw new InvalidElementException(INPUT_NAME_ASSISTANT_CARD);
        }
        int maxMovement = optionalAssistantCard.get().getMaxMovement();
        if (!(distanceToMove >= 1 &&
                distanceToMove <= (ctx.getMutableEffects().isMotherNatureMovementIncreased() ?
                        maxMovement + 2 : maxMovement)
        )) {
            throw new InvalidElementException("DistanceToMove");
        }
        return true;
    }

    @Override
    protected void unsafeExecute(GameBoard ctx) {
        ctx.moveAndActMotherNature(distanceToMove);
    }

}
