package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.FailedOperationException;
import it.polimi.ingsw.Exceptions.InputValidationException;
import it.polimi.ingsw.Exceptions.toremove.InvalidInputException;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.Constants.OPERATION_NAME_CARD01_APPLY_EFFECT;
/*
In setup, draw 4 Students and place them on this card.
EFFECT: Take 1 Student from this card and place it on
an Island of your choice. Then, draw a new Student from the Bag and place it on this card.
 */

public class Card01 extends StatefulEffect {
    @Serial
    private static final long serialVersionUID = 103L; // convention: 1 for model, (01 -> 99) for objects
    private final PawnColour[] students = new PawnColour[4];


    public Card01(GameBoard ctx) {
        super(1, 1, StateType.PAWNCOLOUR, ctx);
    }

    public ArrayList<Object> getState() {
        return new ArrayList<>(Arrays.asList(students));
    }

    public StateType getStateType() {
        return stateType;
    }

    // todo do we need it?
    public void addStudent(PawnColour p) { //add PawnColour into first empty position
        for (int i = 0; i < 4; i++) {
            if (this.students[i] == null) {
                this.students[i] = p;
                return;
            }
        }
    }

    public PawnColour getStudent(int i) {
        if (0 <= i && i < 4) {
            PawnColour student = this.students[i];
            this.students[i] = null;
            return student;
        } else throw new InvalidInputException();
    }

    public boolean checkInput(CharacterCardInput input) throws InputValidationException {
        if (input.getTargetIsland().isEmpty()) {
            throw new InvalidInputException();
        }
        if (input.getTargetPawn().isEmpty()) {
            throw new InvalidInputException();
        }
        // find if the target pawn colour is present in the card's stored pawn
        if (Arrays.stream(this.students).noneMatch(cell -> cell == input.getTargetPawn().get())) {
            throw new InvalidInputException();
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
                this.students[i] = context.getStudentBag().extract();
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
