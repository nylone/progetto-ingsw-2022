package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Enums.GamePhase;
import it.polimi.ingsw.Model.Model;

import java.io.Serial;
import java.util.List;

public class EndTurnOfActionPhase extends PlayerAction {
    @Serial
    private static final long serialVersionUID = 202L; // convention: 2 for controller, (01 -> 99) for objects

    public EndTurnOfActionPhase(int playerBoardId) {
        super(playerBoardId, true);
    }

    @Override
    protected Optional<InputValidationException> customValidation(List<PlayerAction> history, Model ctx) {
        if (
                !(history.stream().
                        filter(playerAction -> playerAction.getClass() == ChooseCloudTile.class)
                        .count() == 1)
        ) {
            return Optional.of(new GenericInputValidationException("History", "ChooseCloudTile action has not been executed"));
        }
        if (!(history.get(history.size() - 1).getClass() == PlayCharacterCard.class || history.get(history.size() - 1).getClass() == ChooseCloudTile.class)) {
            return Optional.of(new GenericInputValidationException("History", "his action can only be executed after a ChooseCloudTile action or PlayCharacterCard action"));
        }

        return Optional.empty();
    }

    @Override
    public void unsafeExecute(Model ctx) {
        // reset effects through EffectTracker
        ctx.getMutableEffects().reset();
        ctx.getMutableTurnOrder().stepToNextPlayer();
        if (ctx.getMutableTurnOrder().getGamePhase() != GamePhase.ACTION) {
            if (ctx.getMutableStudentBag().getSize() > 0)
                ctx.refillClouds();
        }
    }


}
