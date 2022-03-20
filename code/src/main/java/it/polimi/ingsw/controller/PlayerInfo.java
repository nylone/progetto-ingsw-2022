package it.polimi.ingsw.controller;

public class PlayerInfo {

	private final String nickname;
	private final int playerBoardId;

	public PlayerInfo(String nickname, int playerBoardId) {
		this.nickname = nickname;
		this.playerBoardId = playerBoardId;
	}

	public String getNickname() {
		return nickname;
	}

	public int getPlayerBoardID() {
		return playerBoardId;
	}

}
