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


/**
 * Character Card#1
 * In setup, draw 4 Students and place them on this card. <br>
 * EFFECT: Take 1 Student from this card and place it on
 * an Island of your choice. Then, draw a new Student from the Bag and place it on this card.
 */

public class Card01 extends StatefulEffect {
    @Serial
    private static final long serialVersionUID = 103L; // convention: 1 for model, (01 -> 99) for objects
    /**
     * Array containing card's pawns
     */
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

    /**
     * Get card's content
     *
     * @return ArrayList of Objects with pawns (Can be casted to {@link PawnColour})
     */
    public ArrayList<Object> getState() {
        return new ArrayList<>(Arrays.asList(students));
    }

    /**
     * Get card's stateType
     *
     * @return card's stateType
     */
    public StateType getStateType() {
        return stateType;
    }

    /**
     * Refer to: {@link CharacterCard#overridableCheckInput(CharacterCardInput)} for further information
     *
     * @param input CharacterCardInput should contain:
     *              <ul>
     *               <li>A valid island's ID </li>
     *               <li>a valid PawnColour from card</li>
     *              </ul>
     */
    @Override
    public boolean overridableCheckInput(CharacterCardInput input) throws InputValidationException {
        //check if input contains a valid island
        if (input.getTargetIsland().isEmpty()) {
            throw new InvalidElementException("Target Island");
        }
        //check if input contains a valid pawnColour
        if (input.getTargetPawn().isEmpty()) {
            throw new InvalidElementException("Target Pawn Colour");
        }
        Island ti = input.getTargetIsland().get();
        if (ti.getId() < 0 || ti.getId() >= 12) {
            throw new InvalidElementException("Target Island"); // target ti out of bounds for id
        }
        if (!this.context.getMutableIslandField().getMutableIslands().contains(ti)) {
            throw new InvalidElementException("Target Island"); // target ti not in field
        } // note: if island is in field then the island must also be in a group, due to how islandfield works.
        // find if the target pawn colour is present in the card's stored pawn
        if (Arrays.stream(this.students).noneMatch(cell -> cell == input.getTargetPawn().get())) {
            throw new InvalidElementException("Target Pawn Colour");
        }
        //if StudentBag is empty then the card could not be filled anymore
        if (context.getMutableStudentBag().getSize() == 0) {
            throw new GenericInputValidationException("Student Bag", "is empty");
        }
        return true;
    }

    /**
     * Refer to: {@link CharacterCard#unsafeApplyEffect(CharacterCardInput)} for further information
     */
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
        throw new FailedOperationException("Card01.unsafeApplyEffect", "Target pawn was not contained in card's state");
    }

   /*//test-purpose only
    @Override
    public String toString() {
        return "Card01{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
    //*/
}
