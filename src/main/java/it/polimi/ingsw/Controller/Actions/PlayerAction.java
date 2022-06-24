package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
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
     * @param playerBoardID the ID of the player who wishes to interact with the controller.
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

    /**
     * SUB-VALIDATION FUNCTION: <br>
     * if the game is not active anymore (i.e. the game is over and no actions can be made), this function will return a non-empty value.
     *
     * @param ctx the {@link Model} object, used during verification.
     * @return an {@link Optional} value, the value is empty if no issues are found during the validation of the function. Else the
     * value will contain a {@link Throwable} {@link Exception} that can be used to propagate the error message.
     */
    private Optional<InputValidationException> isGameRunning(Model ctx) {
        if (ctx.isGameOver()) {
            return Optional.of(new GenericInputValidationException(this.getClass().getSimpleName(), "Game is over"));
        }
        return Optional.empty();
    }

    /**
     * SUB-VALIDATION FUNCTION: <br>
     * if the {@link PlayerAction}'s declared player is not the current player that needs to play, this function will return a non-empty value.
     *
     * @param ctx the {@link Model} object, used during verification.
     * @return an {@link Optional} value, the value is empty if no issues are found during the validation of the function. Else the
     * value will contain a {@link Throwable} {@link Exception} that can be used to propagate the error message.
     */
    private Optional<InputValidationException> isCorrectTurn(Model ctx) {
        if (!(ctx.getMutablePlayerBoards().size() > this.getPlayerBoardID())) {
            return Optional.of(new InvalidElementException("PlayerBoardID out of range"));
        }
        if (ctx.getMutableTurnOrder().getMutableCurrentPlayer().getId() != this.getPlayerBoardID()) {
            return Optional.of(new GenericInputValidationException(this.getClass().getSimpleName(), "It's not your turn yet"));
        }
        return Optional.empty();
    }

    /**
     * SUB-VALIDATION FUNCTION: <br>
     * if the {@link PlayerAction} is marked as unique per turn, this function will return a non-empty value in case of a duplicate
     * action being present in the history
     *
     * @param history a list of previous actions submitted by the player
     * @return an {@link Optional} value, the value is empty if no issues are found during the validation of the function. Else the
     * value will contain a {@link Throwable} {@link Exception} that can be used to propagate the error message.
     */
    private Optional<InputValidationException> isDuplicate(List<PlayerAction> history) {
        if (!this.uniquePerTurn || history.stream().noneMatch(h -> h.getClass() == this.getClass())) {
            return Optional.empty();
        }
        return Optional.of(new GenericInputValidationException(this.getClass().getSimpleName(), "Too many similar actions have been executed"));
    }

    /**
     * This function is used by {@link #validate(List, Model)} to check whether or not the declared {@link PlayerAction} is possible.<br>
     * This function will check for the following requirements:
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

    /**
     * @return the {@link it.polimi.ingsw.Model.PlayerBoard} id set during construction of the Action.
     */
    final public int getPlayerBoardID() {
        return playerBoardID;
    }

    /**
     * Every class extending {@link PlayerAction} must implement the following method, which takes a {@link Model} reference and
     * applies the concrete effect of the action. <br>
     * Warning: this function, as implied by the name, is unsafe. it should never be called by called outside the scope
     * of the class {@link it.polimi.ingsw.Controller.Controller}, which takes adequate precautions in order to guarantee
     * a coherent execution of the method.
     *
     * @param ctx the {@link Model} reference, once the method finishes running the game state will be altered.
     * @throws Exception Should an error occur during the execution of the method, such error will be reported through the
     *                   thrown {@link Exception}. Note that any {@link PlayerAction} inheritor should guarantee the absence of Exceptions
     *                   for any positive return value yielded by {@link #validate(List, Model)}
     */
    public abstract void unsafeExecute(Model ctx) throws Exception;
}
