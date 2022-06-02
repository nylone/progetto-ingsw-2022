package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.EndTurnOfActionPhase;
import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Controller.Actions.PlayerAction;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Server.Lobby;

import java.util.ArrayList;
import java.util.List;

/**
 * The GameHandler object is the Controller of the whole game. <br>
 * The Controller should be the only entity able to modify the model.
 */
public class Controller {
    private final List<PlayerAction> history;
    private final Model model;

    /**
     * Generates a new instance of Game. This is the default method to call to create a game.
     *
     * @param gameMode the game mode the players are going to use
     * @param lobby the model update listener (called by the model in case of meaningful updates)
     * @param players  a list of maximum 4, minimum 2 strings containing the nicknames of the players
     */
    public Controller(GameMode gameMode, Lobby lobby, String... players) throws InputValidationException {
        this(gameMode, players);
        this.model.addModelUpdateListener(lobby);
    }

    /**
     * Generates a new instance of Game. This is the default method to call to create a game.
     *
     * @param gameMode the game mode the players are going to use
     * @param players  a list of maximum 4, minimum 2 strings containing the nicknames of the players
     */
    public Controller(GameMode gameMode, String... players) throws InputValidationException {
        if (players.length > 1 && players.length <= 4) {
            this.history = new ArrayList<>(6);
            this.model = new Model(gameMode, players);
        } else {
            throw new GenericInputValidationException("Players", "The number of players must be 2, 3 or 4.\n" +
                    "Players received: " + players.length);
        }
    }



    /**
     * Generates a new instance of Game. This is the debug method to call to create a game, since the internal attributes
     * are set to the parameters. <br>
     * <b>Note:</b> this method should be called <b>ONLY</b> by test code.
     *
     * @param game    an instance of GameBoard
     * @param history an instance to a list of PlayerAction, is used by the controller to check the flow of the game
     */
    Controller(GameBoard game, List<PlayerAction> history) {
        this.history = history;
        this.model = new Model(game);
    }

    /**
     * A thread safe execution request. Actions are passed in, validated and executed without risk of deadlocks or
     * undefined behaviours.
     *
     * @param action the action to be validated and (if validation succeeds) to be executed.
     * @throws InputValidationException thrown when validation fails, carries information about the error. If thrown,
     *                                  the model is guaranteed to not have been modified.
     */
    public synchronized void executeAction(PlayerAction action) throws InputValidationException {
        GameBoard gameBoard = this.model.readModel().getGameBoard();
        action.validate(this.getHistory(), gameBoard); // todo validate should return optionals containing validation exceptions
        // as right now we are abusing the hell out of exception throwing
        try {
            this.model.editModel(action::unsafeExecute);
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

    /**
     * Serializes the game model to a new object.
     *
     * @return a copy of the GameBoard object. <br>
     * <b>Note:</b> once called, all changes to the original GameBoard object won't be reflected in the instance returned
     * by this method
     */
    @Deprecated
    GameBoard debugModelReference() {
        return this.model.debugGameBoardReference();
    }

    public int getPlayerBoardIDFromNickname(String nickname) throws InvalidContainerIndexException {
        return this.model.readModel().getPlayerBoardByNickname(nickname).getId();
    }
}
