package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Enums.GamePhase;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.PlayerBoard;

import java.io.Serial;
import java.util.List;

import static it.polimi.ingsw.Misc.Utils.countSimilarClassOccurrences;

/**
 * The Action phase is a lengthy one and its length cannot be determined. This {@link PlayerAction} enables the caller to
 * end an Action phase on its own accord.
 */
public class EndTurnOfActionPhase extends PlayerAction {
    @Serial
    private static final long serialVersionUID = 202L; // convention: 2 for controller, (01 -> 99) for objects

    /**
     * Create a new instance of this class with the following inputs:
     *
     * @param playerBoardId the ID of the current {@link PlayerBoard}
     */
    public EndTurnOfActionPhase(int playerBoardId) {
        super(playerBoardId, true);
    }

    /**
     * {@inheritDoc}
     * <ul>
     *     <li>This action can be called only after having called one and only one {@link ChooseCloudTile} action</li>
     *     <li>The previous {@link PlayerAction}s must be either {@link ChooseCloudTile} or {@link PlayCharacterCard}</li>
     * </ul>
     *
     * @param history the controller stores a {@link List} of previous {@link PlayerAction}s related to the player taking
     *                the current turn (at every new turn, the history is cleared).
     *                Some actions may use this {@link List} to check for duplicates.
     * @param ctx     a reference to {@link Model}. Some actions may use this reference to check for consistency between what
     *                the actions declares and what the Model offers.
     * @return An empty {@link Optional} in case of a successful validation. Otherwise the returned {@link Optional}
     * contains the related {@link InputValidationException}
     */
    @Override
    protected Optional<InputValidationException> customValidation(List<PlayerAction> history, Model ctx) {

        if (countSimilarClassOccurrences(ChooseCloudTile.class, history) != 1) {
            return Optional.of(new GenericInputValidationException("History", "ChooseCloudTile action has not been executed"));
        }
        if (!(history.get(history.size() - 1).getClass() == ChooseCloudTile.class || (history.get(history.size() - 1).getClass() == PlayCharacterCard.class))) {
            return Optional.of(new GenericInputValidationException("History", "this action can only be executed after a ChooseCloudTile action or PlayCharacterCard action"));
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
