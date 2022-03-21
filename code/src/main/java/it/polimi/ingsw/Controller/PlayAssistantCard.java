package it.polimi.ingsw.Controller;

public class PlayAssistantCard extends PlayerAction {

	private final int selectedAssistant;

	public PlayAssistantCard(int playerBoardId, int selectedAssistant) {
		super(playerBoardId);
		this.selectedAssistant = selectedAssistant;
	}
}
