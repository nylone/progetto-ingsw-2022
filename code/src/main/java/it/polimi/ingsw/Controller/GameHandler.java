package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameHandler {

    private final List<PlayerAction> history;
    private GameBoard model;
    private final ByteArrayOutputStream backup;

    public GameHandler(GameMode gameMode, String ... players) {
        this.history = new ArrayList<>(6);
        this.backup = new ByteArrayOutputStream();
        this.model = new GameBoard(gameMode, players);
    }

    public List<PlayerAction> getHistory() {
        return history;
    }

    GameBoard getContext() {
        return model;
    }

    public void executeAction(PlayerAction action) throws InputValidationException {
        action.safeExecute(history, model);
        if (action.getClass() == EndTurnOfActionPhase.class) {
            commitGameState();
        }
    }

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

    public byte[] getSerializedModel() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream writer = new ObjectOutputStream(out);
            writer.writeObject(model);
            return out.toByteArray();
    }

    private void commitGameState() {
        try {
            this.backup.reset();
            ObjectOutputStream serModel = new ObjectOutputStream(this.backup);
            serModel.writeObject(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rollback() {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(this.backup.toByteArray());
            ObjectInputStream serModel = new ObjectInputStream(stream);
            this.model = (GameBoard) serModel.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
