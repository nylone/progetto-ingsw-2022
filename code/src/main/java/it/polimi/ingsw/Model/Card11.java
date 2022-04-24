package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.FailedOperationException;
import it.polimi.ingsw.Exceptions.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.InputValidationException;
import it.polimi.ingsw.Exceptions.toremove.FullDiningRoomException;
import it.polimi.ingsw.Exceptions.toremove.InvalidInputException;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.Constants.*;

/*
In Setup, draw 4 Students and place them on this card
EFFECT: Take 1 Student from this card and place it in
your Dining Room. Then, draw a new Student from the
Bag and place it on this card.

 */
public class Card11 extends StatefulEffect {
    @Serial
    private static final long serialVersionUID = 113L; // convention: 1 for model, (01 -> 99) for objects

    private final PawnColour[] students = new PawnColour[4];

    public Card11(GameBoard ctx) {
        super(11, 2, StateType.PAWNCOLOUR, ctx);
        for (int i = 0; i < 4; i++) {
            this.students[i] = ctx.getStudentBag().extract();
        }
    }

    public ArrayList<Object> getState() {
        return new ArrayList<>(Arrays.asList(students));
    }

    public StateType getStateType() {
        return stateType;
    }

    public boolean checkInput(CharacterCardInput input) throws InputValidationException {
        if (!input.getTargetPawn().isPresent()) {
            throw new InvalidInputException();
        }
        // find if the target pawn colour is present in the card's stored pawn
        if (Arrays.stream(this.students).noneMatch(cell -> cell == input.getTargetPawn().get())) {
            throw new InvalidInputException();
        }

        PlayerBoard playerBoard = input.getCaller();
        // validate size of dining room
        if (!playerBoard.canDiningRoomFit(Arrays.asList(input.getTargetPawn().get()))) {
            throw new GenericInputValidationException(CONTAINER_NAME_DININGROOM,
                    CONTAINER_NAME_DININGROOM + "can't contain " + input.getTargetPawn().get()
                            + "without overflowing.");
        }
        //all tests passed
        return true;
    }

    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        PawnColour movedPawn = input.getTargetPawn().get();
        //add target pawn to caller's dining room
        input.getCaller().addStudentToDiningRoom(input.getTargetPawn().get());
        // find first occurrence of same target pawn in card state and swap it with a new pawn
        for (int i = 0; i < 4; i++) {
            if (this.students[i] == movedPawn) {
                this.students[i] = context.getStudentBag().extract();
                return;
            }
        }
        throw new FailedOperationException(OPERATION_NAME_CARD11_APPLY_EFFECT, "Target pawn was not contained in card's state");
    }

    //todo do we need it?
    private void removeFromCard(PawnColour p) {
        for (int i = 0; i < 4; i++) {
            if (this.students[i].equals(p)) {
                this.students[i] = null;
                break;
            }
        }
    }

    //test-purpose only
    @Override
    public String toString() {
        return "Card11{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
