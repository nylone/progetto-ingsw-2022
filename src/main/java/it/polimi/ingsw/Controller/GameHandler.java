package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.EndTurnOfActionPhase;
import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Controller.Actions.PlayerAction;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.RemoteView.Lobby;

import java.util.ArrayList;
import java.util.List;

/**
 * The GameHandler object is the Controller of the whole game. <br>
 * The Controller should be the only entity able to modify the model.
 */
public class GameHandler {
    private final List<PlayerAction> history;
    private final GameBoard model;

    /**
     * Generates a new instance of Game. This is the default method to call to create a game.
     *
     * @param gameMode the game mode the players are going to use
     * @param players  a list of maximum 4, minimum 2 strings containing the nicknames of the players
     */
    public GameHandler(GameMode gameMode, String... players) throws InputValidationException {
        if (players.length > 1 && players.length <= 4) {
            this.history = new ArrayList<>(6);
            this.model = new GameBoard(gameMode, players);
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
    GameHandler(GameBoard game, List<PlayerAction> history) {
        this.history = history;
        this.model = game;
    }

    public void subscribeLobby(Lobby lobby) {
        this.model.subscribeLobby(lobby);
        this.model.notifyLobby();
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
        if (model.isGameOver()) {
            throw new GenericInputValidationException("GameHandler", "Game is over, action cannot be executed");
        }
        action.safeExecute(getHistory(), model);
        this.model.notifyLobby();

        if (action.getClass() == EndTurnOfActionPhase.class || action.getClass() == PlayAssistantCard.class) {
            this.history.clear();
            return;
        }
        history.add(action);
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
    public GameBoard getModelCopy() {
        return model.getModelCopy();
    }

    public int getPlayerBoardIDFromNickname(String nickname) throws InvalidContainerIndexException {
        return this.model.getMutablePlayerBoardByNickname(nickname).getId();
    }

    public Optional<List<String>> getWinnerNicknames() {
        return model.getWinners().map(list -> list.stream().map(PlayerBoard::getNickname).toList());
    }

    public Optional<List<Integer>> getWinnerIDs() {
        return model.getWinners().map(list -> list.stream().map(PlayerBoard::getId).toList());
    }
}
