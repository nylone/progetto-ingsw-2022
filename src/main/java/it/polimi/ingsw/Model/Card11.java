package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Exceptions.Operation.FailedOperationException;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.Constants.*;

/**
 * In Setup, draw 4 Students and place them on this card <br>
 * EFFECT: Take 1 Student from this card and place it in
 * your Dining Room. Then, draw a new Student from the
 * Bag and place it on this card.
 */
public class Card11 extends StatefulEffect {
    @Serial
    private static final long serialVersionUID = 113L; // convention: 1 for model, (01 -> 99) for objects

    private final PawnColour[] students = new PawnColour[4];

    public Card11(Model ctx) {
        super(11, 2, StateType.PAWNCOLOUR, ctx);
        for (int i = 0; i < 4; i++) {
            try {
                this.students[i] = ctx.getMutableStudentBag().extract();
            } catch (EmptyContainerException e) {
                // should never happen
                Logger.severe("student bag was found empty while adding a student to Card11. Critical, unrecoverable, error");
                throw new RuntimeException(e);
            }
        }
    }

    public ArrayList<Object> getState() {
        return new ArrayList<>(Arrays.asList(students));
    }

    public StateType getStateType() {
        return stateType;
    }

    /**
     * Refer to: {@link CharacterCard#overridableCheckInput(CharacterCardInput)} for further information
     * @param input CharacterCardInput should contain:
     *<ul>
     *  <li>A valid pawnColour from card's state </li>
     * </ul>
     */
    @Override
    public boolean overridableCheckInput(CharacterCardInput input) throws InputValidationException {
        if (input.getTargetPawn().isEmpty()) {
            throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_COLOUR);
        }
        // find if the target pawn colour is present in the card's stored pawn
        if (Arrays.stream(this.students).noneMatch(cell -> cell == input.getTargetPawn().get())) {
            throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_COLOUR);
        }

        PlayerBoard playerBoard = input.getCaller();
        // validate size of dining room
        if (!playerBoard.canDiningRoomFit(input.getTargetPawn().get())) {
            throw new GenericInputValidationException(CONTAINER_NAME_DININGROOM,
                    CONTAINER_NAME_DININGROOM + " can't contain " + input.getTargetPawn().get()
                            + "without overflowing.");
        }
        if (context.getMutableStudentBag().getSize() == 0) {
            throw new GenericInputValidationException(CONTAINER_NAME_STUDENT_BAG, CONTAINER_NAME_STUDENT_BAG + " is empty");
        }
        //all tests passed
        return true;
    }

    /**
     * Refer to: {@link CharacterCard#unsafeApplyEffect(CharacterCardInput)} for further information
     */
    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        PawnColour movedPawn = input.getTargetPawn().get();
        //add target pawn to caller's dining room
        this.context.addStudentToDiningRoom(movedPawn, input.getCaller());
        // find first occurrence of same target pawn in card state and swap it with a new pawn
        for (int i = 0; i < 4; i++) {
            if (this.students[i] == movedPawn) {
                this.students[i] = context.getMutableStudentBag().extract();
                return;
            }
        }
        throw new FailedOperationException(OPERATION_NAME_CARD11_APPLY_EFFECT, "Target pawn was not contained in card's state");
    }

    //test-purpose only
   /* @Override
    public String toString() {
        return "Card11{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }*/
}
