package it.polimi.ingsw.controller;

public class ChooseCloudTile extends PlayerAction {

	private int selectedTile;

	public ChooseCloudTile(int playerBoardId, int selectedTile) {
		super(playerBoardId);
		this.selectedTile = selectedTile;
	}
}
