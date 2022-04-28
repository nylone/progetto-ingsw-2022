package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enums.DestinationType;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

import java.util.List;

import static it.polimi.ingsw.Constants.*;


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
        PlayerBoard caller = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        if(ctx.getMutableTurnOrder().getMutableSelectedCard(caller).isEmpty()){
            throw new GenericInputValidationException(HISTORY, "No PlayAssistantCard has been played");
        }
        if (history.size() > 0) {
            if (!(history.get(history.size() - 1).getClass() == MoveStudent.class || history.get(history.size() - 1).getClass() == PlayCharacterCard.class)) {
                throw new GenericInputValidationException(HISTORY, "MoveStudent can only be preceded by a PlayCharacterCard action or MoveStudent action");
            }
        }
       if (
                history.stream().filter(playerAction -> playerAction.getClass() == PlayCharacterCard.class).count() == 0 &&
                        !(history.stream().filter(playerAction -> playerAction.getClass() == MoveStudent.class).count() < maxCount)
        ) {
            throw new GenericInputValidationException(HISTORY, "only " + maxCount + " pawns can be moved from entrance without playing CharacterCards");
        }

        try {
            caller = ctx.getMutablePlayerBoardById(this.getPlayerBoardId());
        } catch (InvalidContainerIndexException e) {
            throw new InvalidElementException("Target Entrance Position");
        }
        if (!(this.selectedEntrancePosition >= 0 && this.selectedEntrancePosition < entranceSize)) {
            throw new InvalidElementException("Index Target Entrance Position");
        }
        if (caller.getEntranceStudents().get(this.selectedEntrancePosition).isEmpty()) {
            throw new InvalidElementException("Target Entrance Position");
        }

        if (!isCorrectTurn(ctx)) {
            throw new GenericInputValidationException("Action", "the action can only be executed by the current player");
        }
        if (this.destination.getDestinationType() == DestinationType.ISLAND) {
            try {
                if (destination.getIslandID() < 0 || destination.getIslandID() > 12) {
                    throw new InvalidElementException(INPUT_NAME_TARGET_ISLAND); // target ti out of bounds for id
                }
            } catch (Exception e) {
                throw new InvalidElementException("DestinationType not compatible with request");
            }
            int islandId;
            try {
                islandId = this.destination.getIslandID();
            } catch (Exception e) {
                throw new InvalidElementException("DestinationType not compatible with request");
            }
        }
        // validate size of dining room
        if (this.destination.getDestinationType() == DestinationType.DININGROOM) {
            if (caller.isDiningRoomFull(List.of(caller.getEntranceStudents().get(this.selectedEntrancePosition).get()))) {
                throw new GenericInputValidationException(CONTAINER_NAME_DININGROOM,
                        CONTAINER_NAME_DININGROOM + "can't contain the pawn without overflowing on the lane.");
            }
        }
        return true;
    }


    @Override
    protected void unsafeExecute(GameBoard ctx) throws Exception {
        PawnColour toMove = ctx.getMutablePlayerBoardById(this.getPlayerBoardId())
                .getEntranceStudents().get(this.selectedEntrancePosition).get();
        PlayerBoard pb = ctx.getMutablePlayerBoardById(this.getPlayerBoardId());
        // set entrance position to null
        pb.removeStudentFromEntrance(selectedEntrancePosition);
        switch (this.destination.getDestinationType()) {
            case ISLAND -> {
                int id = this.destination.getIslandID();
                ctx.getMutableIslandField().getMutableIslandById(id)
                        .addStudent(toMove);
            }
            case DININGROOM -> ctx.addStudentToDiningRoom(toMove, pb);
        }
    }
}
