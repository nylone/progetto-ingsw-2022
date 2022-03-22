package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.GameMode;

import java.util.ArrayList;

public class GameHandler {

    private GameBoard game;  // todo needs to be init on game start

    private final ArrayList<PlayerAction> actionHistory;

    public GameHandler(GameMode gameMode) {
        this.actionHistory = new ArrayList<>(6);
    }

    public void executeAction(PlayerAction action) {
        //todo
    }

}
