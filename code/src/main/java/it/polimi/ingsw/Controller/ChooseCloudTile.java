package it.polimi.ingsw.Controller;

public class ChooseCloudTile extends PlayerAction {

	private int selectedTile;

	public ChooseCloudTile(int playerBoardId, int selectedTile) {
		super(playerBoardId);
		this.selectedTile = selectedTile;
	}
}
