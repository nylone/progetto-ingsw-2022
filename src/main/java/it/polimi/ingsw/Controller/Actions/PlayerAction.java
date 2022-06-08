package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * The {@link PlayerAction} object is the definition of a user's intention. <br>
 * The various Classes that extend it are the only possible way to interact safely with the model.
 */
public abstract class PlayerAction implements Serializable {
    @Serial
    private static final long serialVersionUID = 200L; // convention: 2 for controller, (01 -> 99) for objects

    private final int playerBoardID;
    private final boolean uniquePerTurn;

    /**
     * Package protected constructor used to initialize the playerBoardID.
     *
     * @param playerBoardID the ID of the player that wishes to interact with the controller.
     * @param uniquePerTurn if set to false, the user can submit multiple actions of the same type during his turn.
     */
    protected PlayerAction(int playerBoardID, boolean uniquePerTurn) {
        this.playerBoardID = playerBoardID;
        this.uniquePerTurn = uniquePerTurn;
    }

    /**
     * The validate function is used to check whether or not the declared {@link PlayerAction} is possible.<br>
     * The validate function will check for:
     * <ul>
     *     <li>The game must not be over</li>
     *     <li>The action must be called during the correct turn</li>
     *     <li>If the action is unique, check that no duplicates have been played before</li>
     *     <li>Any additional constraint imposed by {@link #customValidation(List, Model)}</li>
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
    public final Optional<InputValidationException> validate(List<PlayerAction> history, Model ctx) {
        Optional<InputValidationException> gameRunningCheck = isGameRunning(ctx);
        if (gameRunningCheck.isPresent()) return gameRunningCheck;

        Optional<InputValidationException> correctTurnCheck = isCorrectTurn(ctx);
        if (correctTurnCheck.isPresent()) return correctTurnCheck;

        Optional<InputValidationException> duplicateCheck = isDuplicate(history);
        if (duplicateCheck.isPresent()) return duplicateCheck;

        Optional<InputValidationException> customValidationCheck = customValidation(history, ctx);
        if (customValidationCheck.isPresent()) return customValidationCheck;

        return Optional.empty();
    }

    private Optional<InputValidationException> isGameRunning(Model ctx) {
        if (ctx.isGameOver()) {
            return Optional.of(new GenericInputValidationException("GameHandler", "Game is over"));
        }
        return Optional.empty();
    }

    private Optional<InputValidationException> isCorrectTurn(Model ctx) {
        if (ctx.getMutableTurnOrder().getMutableCurrentPlayer().getId() == this.getPlayerBoardID()) {
            return Optional.empty();
        }
        return Optional.of(new GenericInputValidationException("GameHandler", "It's not your turn yet"));
    }

    private Optional<InputValidationException> isDuplicate(List<PlayerAction> history) {
        if (!this.uniquePerTurn || history.stream().noneMatch(h -> h.getClass() == this.getClass())) {
            return Optional.empty();
        }
        return Optional.of(new GenericInputValidationException("GameHandler", "Too many similar actions have been executed"));
    }

    /**
     * This function is used by {@link #validate(List, Model)} to check whether or not the declared {@link PlayerAction} is possible.<br>
     * This function will check for:
     *
     * @param history the controller stores a {@link List} of previous {@link PlayerAction}s related to the player taking
     *                the current turn (at every new turn, the history is cleared).
     *                Some actions may use this {@link List} to check for duplicates.
     * @param ctx     a reference to {@link Model}. Some actions may use this reference to check for consistency between what
     *                the actions declares and what the Model offers.
     * @return An empty {@link Optional} in case of a successful validation. Otherwise the returned {@link Optional}
     * contains the related {@link InputValidationException}
     */
    protected abstract Optional<InputValidationException> customValidation(List<PlayerAction> history, Model ctx);

    final public int getPlayerBoardID() {
        return playerBoardID;
    }

    public abstract void unsafeExecute(Model ctx) throws Exception;
}
