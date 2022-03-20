package it.polimi.ingsw.controller;

public class MoveMotherNature extends PlayerAction {

	private final int distanceToMove;

	public MoveMotherNature(int playerBoardId, int distanceToMove) {
		super(playerBoardId);
		this.distanceToMove = distanceToMove;
	}
}
