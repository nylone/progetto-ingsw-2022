package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

import java.util.List;

public class MoveStudent extends PlayerAction {

    private final int selectedEntrancePosition;
    private final MoveDestination destination;

    public MoveStudent(int playerBoardId, int selectedEntrancePosition, MoveDestination destination) {
        super(playerBoardId);
        this.selectedEntrancePosition = selectedEntrancePosition;
        this.destination = destination;
    }

    @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) {
        // super.validate(history, ctx)
        // note: this method is not to be called as it fails if there are multiple actions of same type in history
        // and this class will be present at least 3 times in the history as the turn is completed

        int maxCount = ctx.getPlayerBoards().size() == 3 ? 4 : 3;
        int entranceSize = ctx.getPlayerBoards().size() == 3 ? 9 : 7;
        return isCorrectTurn(ctx) &&
                countDuplicateActions(history) < maxCount &&
                this.selectedEntrancePosition < entranceSize &&
                ctx.getPlayerBoardById(this.getPlayerBoardId())
                        .getEntranceStudents().get(this.selectedEntrancePosition) != null; // todo implement optionals in getentrance
    }

    @Override
    protected void unsafeExecute(GameBoard ctx) throws Exception {
        PawnColour toMove = ctx.getPlayerBoardById(this.getPlayerBoardId())
                .getEntranceStudents().get(this.selectedEntrancePosition);
        PlayerBoard pb = ctx.getPlayerBoardById(this.getPlayerBoardId());
        // set entrance position to null
        pb.removeStudentFromEntrance(selectedEntrancePosition);
        switch (this.destination.getDestinationType()) {
            case ISLAND -> {
                int id = this.destination.getIslandID();
                ctx.getIslandField().getIslandById(id)
                        .addStudent(toMove);
            }
            case DININGROOM -> {
                pb.addStudentToDiningRoom(toMove);
            }
        }
    }
}
