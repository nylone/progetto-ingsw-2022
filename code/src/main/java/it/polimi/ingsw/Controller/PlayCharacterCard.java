package it.polimi.ingsw.Controller;

public class PlayCharacterCard extends PlayerAction {

	private final int selectedCard;

	public PlayCharacterCard(int playerBoardId, int selectedCard) {
		super(playerBoardId);
		this.selectedCard = selectedCard;
	}
}
