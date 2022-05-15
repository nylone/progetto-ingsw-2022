package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GamePhase;
import it.polimi.ingsw.Model.GameBoard;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class EndTurnOfActionPhase extends PlayerAction {
    @Serial
    private static final long serialVersionUID = 202L; // convention: 2 for controller, (01 -> 99) for objects
    public EndTurnOfActionPhase(int playerBoardId) {
        super(playerBoardId);
    }

    @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) throws InputValidationException {
        if (
                !(history.stream().
                        filter(playerAction -> playerAction.getClass() == ChooseCloudTile.class)
                        .count() == 1)
        ) {
            throw new GenericInputValidationException("History", "ChooseCloudTile action has not been executed");
        }
        if (!(history.get(history.size() - 1).getClass() == PlayCharacterCard.class || history.get(history.size() - 1).getClass() == ChooseCloudTile.class)) {
            throw new GenericInputValidationException("History", "his action can only be executed after a ChooseCloudTile action or PlayCharacterCard action");
        }

        if (!super.validate(history, ctx)) {
            throw new GenericInputValidationException("Action", "this action can't be executed more than once or be executed by other player than the current");
        }
        return true;
    }

    @Override
    protected void unsafeExecute(GameBoard ctx) {
        // reset effects through EffectTracker
        ctx.getMutableEffects().reset();
        ctx.getMutableTurnOrder().stepToNextPlayer();
        if (ctx.getMutableTurnOrder().getGamePhase() != GamePhase.ACTION) {
            if (ctx.getMutableStudentBag().getSize() > 0)
                ctx.refillClouds();
        }
    }


}
