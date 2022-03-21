package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.GameMode;

import java.util.ArrayList;

public class GameHandler {

	private GameBoard game;

	private ArrayList<PlayerInfo> players;

	private ArrayList<PlayerAction> actionHistory;

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
