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
    private ArrayList<PlayerInfo> players;
    private ByteArrayOutputStream backup;

    public GameHandler(GameMode gameMode) {
        this.history = new ArrayList<PlayerAction>(6);
    }

    public List<PlayerAction> getHistory() {
        return history;
    }

    public void executeAction(PlayerAction action) throws InputValidationException {
        action.safeExecute(history, model);
        if (action.getClass() == EndTurnOfActionPhase.class) {
            commitGameState();
        }
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
