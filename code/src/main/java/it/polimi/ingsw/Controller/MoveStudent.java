package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;

public class MoveStudent extends PlayerAction {

    private final int selectedEntrancePosition;

    private final MoveDestination destination;

    public MoveStudent(int playerBoardId, int selectedEntrancePosition, MoveDestination destination) {
        super(playerBoardId);
        this.selectedEntrancePosition = selectedEntrancePosition;
        this.destination = destination;
    }

    public void execute(GameBoard ctx) {

    }
}
