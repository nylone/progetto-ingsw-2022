package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.DestinationType;
import it.polimi.ingsw.Controller.MoveDestination;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.SerializableOptional;
import it.polimi.ingsw.Model.Enums.GamePhase;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.PlayerBoard;

import java.io.Serial;
import java.util.List;

import static it.polimi.ingsw.Constants.*;
import static it.polimi.ingsw.Misc.Utils.countSimilarClassOccurrences;

/**
 * This {@link PlayerAction} allows the caller to move a single student from the entrance to the school to an island or a dining room table. This action
 * is linked to the Action Phase.
 */
public class MoveStudent extends PlayerAction {
    @Serial
    private static final long serialVersionUID = 205L; // convention: 2 for controller, (01 -> 99) for objects

    private final int selectedEntrancePosition;
    private final MoveDestination destination;

    /**
     * Create a new instance of this class with the following inputs:
     *
     * @param playerBoardId            the ID of the current {@link PlayerBoard}
     * @param destination              a {@link MoveDestination} value specifying where the selected student will be moved
     * @param selectedEntrancePosition id of the slot in the list returned by {@link PlayerBoard#getEntranceStudents()}, selects a student to be moved
     */
    public MoveStudent(int playerBoardId, int selectedEntrancePosition, MoveDestination destination) {
        super(playerBoardId, false);
        this.selectedEntrancePosition = selectedEntrancePosition;
        this.destination = destination;
    }

    /**
     * {@inheritDoc}
     * <ul>
     *     <li>The {@link GamePhase} must be {@link GamePhase#ACTION}</li>
     *     <li>The previous {@link PlayerAction}s must be either {@link MoveStudent} or {@link PlayCharacterCard} or the history must be empty</li>
     *     <li>Only a limited number of actions like this one can be played (3 or 4 depending on the players)</li>
     *     <li>The student decleared to move must be part of the entrance on the {@link PlayerBoard}</li>
     *     <li>If the destination is {@link DestinationType#ISLAND} then the island ID must be within bounds (0 to 12 excluded)</li>
     *     <li>If the destination is {@link DestinationType#DININGROOM} then the dining room must be able to contain the pawn</li>
     * </ul>
     *
     * @param history the controller stores a {@link List} of previous {@link PlayerAction}s related to the player taking
     *                the current turn (at every new turn, the history is cleared).
     *                Some actions may use this {@link List} to check for duplicates.
     * @param ctx     a reference to {@link Model}. Some actions may use this reference to check for consistency between what
     *                the actions declares and what the Model offers.
     * @return An empty {@link SerializableOptional} in case of a successful validation. Otherwise the returned {@link SerializableOptional}
     * contains the related {@link InputValidationException}
     */
    @Override
    protected SerializableOptional<InputValidationException> customValidation(List<PlayerAction> history, Model ctx) {
        int maxCount = ctx.getMutablePlayerBoards().size() == 3 ? 4 : 3;
        int entranceSize = ctx.getMutablePlayerBoards().size() == 3 ? 9 : 7;
        PlayerBoard caller = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        if (ctx.getMutableTurnOrder().getGamePhase() != GamePhase.ACTION) {
            return SerializableOptional.of(new GenericInputValidationException("GamePhase", "the game is not in the correct phase"));
        }
        if (history.size() > 0) {
            if (!(history.get(history.size() - 1).getClass() == MoveStudent.class || history.get(history.size() - 1).getClass() == PlayCharacterCard.class)) {
                return SerializableOptional.of(new GenericInputValidationException(HISTORY, "MoveStudent can only be preceded by a PlayCharacterCard action or MoveStudent action"));
            }
        }
        if (countSimilarClassOccurrences(MoveStudent.class, history) >= maxCount) {
            return SerializableOptional.of(new GenericInputValidationException(HISTORY, "only " + maxCount + " pawns can be moved from entrance"));
        }

        if (!(this.selectedEntrancePosition >= 0 && this.selectedEntrancePosition < entranceSize)) {
            return SerializableOptional.of(new InvalidElementException("Index Target Entrance Position"));
        }
        if (caller.getEntranceStudents().get(this.selectedEntrancePosition).isEmpty()) {
            return SerializableOptional.of(new InvalidElementException("Target Entrance Position"));
        }

        if (this.destination.getDestinationType() == DestinationType.ISLAND) {
            int islandId = this.destination.getIslandID();
            if (islandId < 0 || islandId > 12) {
                return SerializableOptional.of(new InvalidElementException(INPUT_NAME_TARGET_ISLAND)); // target ti out of bounds for id
            }
        } else if (this.destination.getDestinationType() == DestinationType.DININGROOM) {
            if (!caller.canDiningRoomFit(caller.getEntranceStudents().get(this.selectedEntrancePosition).get())) {
                return SerializableOptional.of(new GenericInputValidationException(CONTAINER_NAME_DININGROOM,
                        CONTAINER_NAME_DININGROOM + "can't contain the pawn without overflowing."));
            }
        }
        return SerializableOptional.empty();
    }


    @Override
    public void unsafeExecute(Model ctx) throws Exception {
        PawnColour toMove = ctx.getMutablePlayerBoardById(this.getPlayerBoardID())
                .getEntranceStudents().get(this.selectedEntrancePosition).get();
        PlayerBoard pb = ctx.getMutablePlayerBoardById(this.getPlayerBoardID());
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
