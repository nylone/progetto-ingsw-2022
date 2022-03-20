package it.polimi.ingsw.controller;

public class PlayAssistantCard extends PlayerAction {

	private final int selectedAssistant;

	public PlayAssistantCard(int playerBoardId, int selectedAssistant) {
		super(playerBoardId);
		this.selectedAssistant = selectedAssistant;
	}
}
