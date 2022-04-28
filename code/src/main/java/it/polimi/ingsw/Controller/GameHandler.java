package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The GameHandler object is the Controller of the whole game. <br>
 * The Controller should be the only entity able to modify the model.
 */
public class GameHandler {

    private final List<PlayerAction> history;
    private final ByteArrayOutputStream backup;
    private GameBoard model;

    /**
     * Generates a new instance of Game. This is the default method to call to create a game.
     *
     * @param gameMode the game mode the players are going to use
     * @param players  a list of maximum 4, minimum 2 strings containing the nicknames of the players
     */
    public GameHandler(GameMode gameMode, String... players) {
        this.history = new ArrayList<>(6);
        this.backup = new ByteArrayOutputStream();
        this.model = new GameBoard(gameMode, players);
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
        this.backup = new ByteArrayOutputStream();
        this.model = game;
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
        action.safeExecute(getHistory(), model);
        if (action.getClass() == EndTurnOfActionPhase.class || action.getClass() == PlayAssistantCard.class) {
            commitGameState();
            this.history.clear();
            return;
        }
        history.add(action);
    }

    public void addCoinToBalance(int amount){
        model.addCoinToReserve(amount);
    }


    /**
     * @return an immutable copy of the list of player actions.<br>
     * <b>Note:</b> the single actions are immutable by default, so do not get cloned
     */
    private List<PlayerAction> getHistory() {
        return List.copyOf(history);
    }

    /**
     * Commits the current game state as the backup state for the controller. Useful when handling disconnections.
     */
    private void commitGameState() {
        try {
            this.backup.reset();
            ObjectOutputStream serModel = new ObjectOutputStream(this.backup);
            serModel.writeObject(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Serializes the game model to a new object.
     *
     * @return a copy of the GameBoard object. <br>
     * <b>Note:</b> once called, all changes to the original GameBoard object won't be reflected in the instance returned
     * by this method
     */
    public GameBoard getModelCopy() {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(getSerializedModel());
            ObjectInputStream reader = new ObjectInputStream(stream);
            return (GameBoard) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // never executed
    }

    /**
     * Serializes the game model to a new de-serializable byte array.
     *
     * @return a copy of the GameBoard object. <br>
     * <b>Note:</b> once called, all changes to the original GameBoard object won't be reflected in the instance returned
     * by this method
     */
    public byte[] getSerializedModel() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream writer = new ObjectOutputStream(out);
        writer.writeObject(model);
        return out.toByteArray();
    }

    /**
     * When called, signals the GameHandler that a player has disconnected.
     * This event is not treated as an action since the game state might have to be fetched from a backup.
     *
     * @param nickname the nickname of the disconnected player
     */
    protected void handleDisconnection(String nickname) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(this.backup.toByteArray());
            ObjectInputStream serModel = new ObjectInputStream(stream);
            this.model = (GameBoard) serModel.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
