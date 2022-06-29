package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.EndTurnOfActionPhase;
import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Controller.Actions.PlayerAction;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.SerializableOptional;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.ModelWrapper;
import it.polimi.ingsw.Server.Lobby;
import it.polimi.ingsw.Server.Messages.Events.Internal.GameOverEvent;
import it.polimi.ingsw.Server.Messages.Events.Internal.ModelUpdateEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is the Controller of the whole game. <br>
 * The Controller should be the only entity able to modify the model.
 */
public class Controller {
    private final List<PlayerAction> history;
    private final ModelWrapper modelWrapper;
    private boolean unsafeReferences = false;

    /**
     * Subscribes a new {@link Controller} object to a {@link ModelWrapper} instance, allowing the creation of a
     * Controller to Model connection.
     *
     * @param modelWrapper an instance of {@link ModelWrapper}
     */
    private Controller(ModelWrapper modelWrapper) {
        Objects.requireNonNull(modelWrapper);

        this.history = new ArrayList<>(6);
        this.modelWrapper = modelWrapper;
    }

    /**
     * Generates a new instance of the {@link Controller}. This is the debug method to call to create a game, since the internal attributes
     * are set to the parameters.<br>
     *
     * <b>Note:</b> this method will not protect model references after editing actions to the model.
     * <b>Note:</b> this method should be called <b>ONLY</b> by test code.
     *
     * @param modelWrapper an instance of {@link ModelWrapper}
     * @param history      an instance to a list of {@link PlayerAction}, used by the controller to check the flow of the game
     */
    Controller(ModelWrapper modelWrapper, List<PlayerAction> history) {
        this.history = history;
        this.modelWrapper = modelWrapper;
        this.unsafeReferences = true;
    }

    /**
     * Generates a new instance. This is the static method to call for general purpose creation of a game.
     *
     * @param gameMode the game mode the players are going to use
     * @param lobby    in case a server is used, insert the {@linkplain Lobby} object wrapped in an {@link SerializableOptional} to let it
     *                 receive {@link ModelUpdateEvent} and {@link GameOverEvent}
     * @param players  a list of minimum 2 and maximum 4 strings containing the nicknames of the players.
     *                 In the case of 4 players: players at index 0 and 2 go together against players at index 1 and 3
     * @throws InputValidationException if the supplied players are less than 2 or more than 4
     */
    public static Controller createGame(GameMode gameMode, SerializableOptional<Lobby> lobby, String... players) throws InputValidationException {
        Objects.requireNonNull(gameMode);
        Objects.requireNonNull(lobby);
        Objects.requireNonNull(players);

        if (players.length > 1 && players.length <= 4) {
            return new Controller(new ModelWrapper(new Model(gameMode, players), lobby));
        } else {
            throw new GenericInputValidationException("Players", "The number of players must be 2, 3 or 4.\n" +
                    "Players received: " + players.length);
        }
    }

    /**
     * An execution request handler. Actions are passed in, validated and (if possible) executed. <br>
     * Warning: this request is not thread safe, that job is delegated to the caller to handle.
     * <p>
     * Note: if this Controller was generated using the debug constructor, then references to the model, once modified, are
     * going to be kept unsafe, generally decreasing the security of the editing mechanism.
     *
     * @param action the action to be validated and executed.
     * @throws InputValidationException thrown when validation fails, carries information about the error. If thrown,
     *                                  the model is guaranteed to not have been modified.
     */
    public void executeAction(PlayerAction action) throws InputValidationException {
        Model model = this.modelWrapper.modelCopy(false);
        SerializableOptional<InputValidationException> validation = action.validate(this.getHistory(), model);
        if (validation.isPresent()) throw validation.get();
        // as right now we are abusing the hell out of exception throwing
        try {
            this.modelWrapper.editModel(action::unsafeExecute, unsafeReferences);
        } catch (Exception e) {
            e.printStackTrace();
        }

        history.add(action);
        if (action.getClass() == EndTurnOfActionPhase.class || action.getClass() == PlayAssistantCard.class) {
            this.history.clear();
        }
    }

    /**
     * @return an immutable copy of the list of player actions.<br>
     * <b>Note:</b> the single actions are immutable by default, so do not get cloned
     */
    private List<PlayerAction> getHistory() {
        return List.copyOf(history);
    }
}
