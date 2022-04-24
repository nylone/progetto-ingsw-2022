package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.toremove.FullEntranceException;
import it.polimi.ingsw.Exceptions.toremove.InvalidInputException;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;

/*
In Setup, draw 6 Students and place them on this card
EFFECT: you may take up to 3 students from this card and replace them with the same number of Students
from your Entrance
 */
public class Card07 extends StatefulEffect {
    @Serial
    private static final long serialVersionUID = 109L; // convention: 1 for model, (01 -> 99) for objects

    private final PawnColour[] students = new PawnColour[6];

    public Card07(GameBoard ctx) {
        super(7, 1, StateType.PAWNCOLOUR, ctx);
        for (int i = 0; i < 6; i++) {
            this.students[i] = ctx.getStudentBag().extract();
        }
    }

    public ArrayList<Object> getState() {
        return new ArrayList<>(Arrays.asList(students));
    }

    public StateType getStateType() {
        return stateType;
    }

    public void overridableCheckInput(CharacterCardInput input) {
        if (!input.getTargetPawnPairs().isPresent()) {
            throw new InvalidInputException();
        } else {
            if (input.getTargetPawnPairs().get()[0].length <= 3 && input.getTargetPawnPairs().get()[1].length <= 3) {
                //convention of input.targetPawnPairs ---> index 0 students from PlayerBoard/ index 1 students from Card
                removeFromCard(input.getTargetPawnPairs().get()[1]);
                int cont = 0;
                for (int i = 0; i < 6; i++) {
                    if (this.students[i] == null) {
                        this.students[i] = input.getTargetPawnPairs().get()[0][cont]; //adding students from PlayerBoard's entrance into the card
                        cont++;
                        if (cont == input.getTargetPawnPairs().get()[0].length) break;
                    }
                }
                try { //adding student from Card to PlayerBoard's entrance
                    input.getCaller().addStudentsToEntrance(new ArrayList<>(Arrays.asList(input.getTargetPawnPairs().get()[1])));
                } catch (FullEntranceException ex) {
                    ex.printStackTrace();
                }
                addUse();
            } else {
                throw new InvalidInputException("More than 3 pairs");
            }
        }
    }

    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {

    }

    private void removeFromCard(PawnColour[] pawns) {
        int cont = 0;
        for (int i = 0; i < 6; i++) {
            if (this.students[i].equals(pawns[cont])) {
                this.students[i] = null;
                cont++;
                if (cont == pawns.length) break;
            }
        }
    }

    //test-purpose only
    @Override
    public String toString() {
        return "Card07{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
