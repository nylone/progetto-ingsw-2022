package it.polimi.ingsw.Controller;

public class MoveStudent extends PlayerAction {

	private final int selectedEntrancePosition;

	private final MoveDestination destination;

	public MoveStudent(int playerBoardId, int selectedEntrancePosition, MoveDestination destination) {
		super(playerBoardId);
		this.selectedEntrancePosition = selectedEntrancePosition;
		this.destination = destination;
	}

}
