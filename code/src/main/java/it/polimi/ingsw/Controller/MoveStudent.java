package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enums.DestinationType;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.Constants.CONTAINER_NAME_DININGROOM;
import static it.polimi.ingsw.Constants.INPUT_NAME_TARGET_ISLAND;


public class MoveStudent extends PlayerAction {

    private final int selectedEntrancePosition;
    private final MoveDestination destination;

    public MoveStudent(int playerBoardId, int selectedEntrancePosition, MoveDestination destination) {
        super(playerBoardId);
        this.selectedEntrancePosition = selectedEntrancePosition;
        this.destination = destination;
    }


    @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) throws InputValidationException {
        // super.validate(history, ctx)
        // note: this method is not to be called as it fails if there are multiple actions of same type in history
        // and this class will be present at least 3 times in the history as the turn is completed

        int maxCount = ctx.getMutablePlayerBoards().size() == 3 ? 4 : 3;
        int entranceSize = ctx.getMutablePlayerBoards().size() == 3 ? 9 : 7;
        PlayerBoard caller;
        try {
            caller = ctx.getMutablePlayerBoardById(this.getPlayerBoardId());
        } catch (InvalidContainerIndexException e) {
            throw new InvalidElementException("Target Entrance Position");
        }
        if(!(this.selectedEntrancePosition>=0 && this.selectedEntrancePosition < entranceSize )){
            throw new InvalidElementException("Index Target Entrance Position");
        }
        if(!(countDuplicateActions(history) <= maxCount)){
            throw new GenericInputValidationException("Action", "this action can't be executed more than "+maxCount+ " times");
        }
        if(caller.getEntranceStudents().get(this.selectedEntrancePosition) == null){ // todo implement optionals in getentrance
            throw new InvalidElementException("Target Entrance Position");
        }

        if(!isCorrectTurn(ctx)){
            throw new GenericInputValidationException("Action", "the action can only be executed by the current player");
        }
        if(this.destination.getDestinationType() == DestinationType.ISLAND){
            int islandId;
            try {
                islandId = this.destination.getIslandID();
            } catch (Exception e) {
                throw new InvalidElementException("DestinationType not compatible with request");
            }
            if(islandId < 0 && islandId > 12){
                throw new InvalidElementException(INPUT_NAME_TARGET_ISLAND); // target ti out of bounds for id
            }
        }
        // validate size of dining room
        if(this.destination.getDestinationType() == DestinationType.DININGROOM){
            if(caller.isDiningRoomFull(List.of(caller.getEntranceStudents().get(this.selectedEntrancePosition)))){
                throw new GenericInputValidationException(CONTAINER_NAME_DININGROOM,
                        CONTAINER_NAME_DININGROOM + "can't contain the pawn without overflowing on the lane.");
            }
        }
        return true;
    }


    @Override
    protected void unsafeExecute(GameBoard ctx) throws Exception {
        PawnColour toMove = ctx.getMutablePlayerBoardById(this.getPlayerBoardId())
                .getEntranceStudents().get(this.selectedEntrancePosition);
        PlayerBoard pb = ctx.getMutablePlayerBoardById(this.getPlayerBoardId());
        // set entrance position to null
        pb.removeStudentFromEntrance(selectedEntrancePosition);
        switch (this.destination.getDestinationType()) {
            case ISLAND -> {
                int id = this.destination.getIslandID();
                ctx.getMutableIslandField().getMutableIslandById(id)
                        .addStudent(toMove);
            }
            case DININGROOM -> {
                ctx.addStudentToDiningRoom(toMove, pb);
            }
        }
    }
}
