package it.polimi.ingsw.controller;

public abstract class PlayerAction {

	private final int playerBoardId;

	public PlayerAction(int playerBoardId) {
		this.playerBoardId = playerBoardId;
	}

	public int getPlayerBoardId() {
		return playerBoardId;
	}

}
