package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;

import java.util.ArrayList;
import java.util.List;

public class GameHandler {

    private final List<PlayerAction> history;
    private GameBoard model;
    private ArrayList<PlayerInfo> players;

    public GameHandler(GameMode gameMode) {
        this.history = new ArrayList<PlayerAction>(6);
    }

    public void addPlayer(String nickname) {
        //todo
    }

    public void executeAction(PlayerAction action) {
        if (action.validate(history, model)) action.execute(model);
    }
    public void rollback(){
        //todo
    }

}
