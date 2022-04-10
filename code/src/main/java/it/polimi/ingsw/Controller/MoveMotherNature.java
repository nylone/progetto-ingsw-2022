package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;

public class MoveMotherNature extends PlayerAction {

    private final int distanceToMove;

    public MoveMotherNature(int playerBoardId, int distanceToMove) {
        super(playerBoardId);
        this.distanceToMove = distanceToMove;
    }

    //todo check
    public void executeAction(GameBoard ctx) {
        ctx.moveMotherNature(distanceToMove); //todo by ale: non so se il controller debba fare così :(
    }


}
