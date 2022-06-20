package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Enums.GamePhase;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.PlayerBoard;

import java.io.Serial;
import java.util.List;

import static it.polimi.ingsw.Misc.Utils.countSimilarClassOccurrences;

public class MoveMotherNature extends PlayerAction {
    @Serial
    private static final long serialVersionUID = 204L; // convention: 2 for controller, (01 -> 99) for objects

    private final int distanceToMove;

    public MoveMotherNature(int playerBoardId, int distanceToMove) {
        super(playerBoardId, true);
        this.distanceToMove = distanceToMove;
    }

    /**
     * {@inheritDoc}
     * <ul>
     *     <li>This action can be called only after having used all possible {@link MoveStudent} actions</li>
     *     <li>The previous {@link PlayerAction}s must be either {@link MoveStudent} or {@link PlayCharacterCard}</li>
     *     <li>The distance declared to move must be within acceptable ranges</li>
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
        PlayerBoard currentPlayer = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        int maxCount = ctx.getMutablePlayerBoards().size() == 3 ? 4 : 3;
        Optional<AssistantCard> optionalAssistantCard = ctx.getMutableTurnOrder().getMutableSelectedCard(currentPlayer);
        if (!(countSimilarClassOccurrences(MoveStudent.class, history) == maxCount)) {
            return Optional.of(new GenericInputValidationException("History", "MotherNature can't be moved before having placed all " + maxCount + " pawns"));
        }
        if (!(history.get(history.size() - 1).getClass() == MoveStudent.class || (history.get(history.size() - 1).getClass() == PlayCharacterCard.class))) {
            return Optional.of(new GenericInputValidationException("History", "This action can only be executed after a MoveStudent action or PlayCharacterCard action"));
        }
        int maxMovement = optionalAssistantCard.get().getMaxMovement();
        if (!(distanceToMove >= 1 &&
                distanceToMove <= (ctx.getMutableEffects().isMotherNatureMovementIncreased() ?
                        maxMovement + 2 : maxMovement)
        )) {
            return Optional.of(new InvalidElementException("DistanceToMove"));
        }
        return Optional.empty();
    }

    @Override
    public void unsafeExecute(Model ctx) {
        ctx.moveAndActMotherNature(distanceToMove);
    }

}
