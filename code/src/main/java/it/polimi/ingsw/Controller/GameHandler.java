package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.GameMode;

import java.util.ArrayList;

public class GameHandler {

    private final GameBoard game;

    private final ArrayList<PlayerInfo> players;

    private final ArrayList<PlayerAction> actionHistory;

    public GameHandler(GameMode gameMode) {
        this.game = new GameBoard(gameMode);
        this.players = new ArrayList<>(2);
        this.actionHistory = new ArrayList<>(6);
    }

    public void addPlayer(String nickname) {

    }

    public void executeAction(PlayerAction action) {

    }

}
