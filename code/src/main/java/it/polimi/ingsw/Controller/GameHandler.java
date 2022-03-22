package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.GameMode;

import java.util.ArrayList;

public class GameHandler {

    private GameBoard game;  // todo needs to be init on game start

    private final ArrayList<PlayerInfo> players;

    private final ArrayList<PlayerAction> actionHistory;

    public GameHandler(GameMode gameMode) {
        this.players = new ArrayList<>(2);
        this.actionHistory = new ArrayList<>(6);
    }

    public void addPlayer(String nickname) {
        this.players.add(new PlayerInfo(nickname, this.players.size()));
    }

    public void executeAction(PlayerAction action) {
        //todo
    }

}
