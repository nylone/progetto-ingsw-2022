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
 * In setup, draw 4 Students and place them on this card. <br>
 * EFFECT: Take 1 Student from this card and place it on
 * an Island of your choice. Then, draw a new Student from the Bag and place it on this card.
 */

public class Card01 extends StatefulEffect {
    @Serial
    private static final long serialVersionUID = 103L; // convention: 1 for model, (01 -> 99) for objects
    private final PawnColour[] students = new PawnColour[4];

    public Card01(Model ctx) {
        super(1, 1, StateType.PAWNCOLOUR, ctx);
        for (int i = 0; i < 4; i++) {
            try {
                this.students[i] = ctx.getMutableStudentBag().extract();
            } catch (EmptyContainerException e) {
                // should never happen
                Logger.severe("student bag was found empty while adding a student to Card01. Critical, unrecoverable, error");
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

    @Override
    public boolean overridableCheckInput(CharacterCardInput input) throws InputValidationException {
        if (input.getTargetIsland().isEmpty()) {
            throw new InvalidElementException(INPUT_NAME_TARGET_ISLAND);
        }
        if (input.getTargetPawn().isEmpty()) {
            throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_COLOUR);
        }
        // find if the target pawn colour is present in the card's stored pawn
        if (Arrays.stream(this.students).noneMatch(cell -> cell == input.getTargetPawn().get())) {
            throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_COLOUR);
        }
        if (context.getMutableStudentBag().getSize() == 0) {
            throw new GenericInputValidationException(CONTAINER_NAME_STUDENT_BAG, CONTAINER_NAME_STUDENT_BAG + "is empty");
        }
        return true;
    }

    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        PawnColour movedPawn = input.getTargetPawn().get();
        // add target pawn to island
        input.getTargetIsland().get().addStudent(movedPawn);
        // find first occurrence of same target pawn in card state and swap it with a new pawn
        for (int i = 0; i < 4; i++) {
            if (this.students[i] == movedPawn) {
                this.students[i] = context.getMutableStudentBag().extract();
                return; // repeat this action only once per loop
            }
        }
        throw new FailedOperationException(OPERATION_NAME_CARD01_APPLY_EFFECT, "Target pawn was not contained in card's state");
    }

    //test-purpose only
    @Override
    public String toString() {
        return "Card01{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
