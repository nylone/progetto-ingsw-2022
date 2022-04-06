package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.FullEntranceException;
import it.polimi.ingsw.Exceptions.InvalidInputException;

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
        for(int i=0; i<6; i++){
            this.students[i] = ctx.getStudentBag().extract();
        }
    }

    public ArrayList<Object> getState() {
        return new ArrayList<>(Arrays.asList(students));
    }

    public StateType getStateType() {
        return stateType;
    }

    public void Use(CharacterCardInput input) {
        if(!input.getTargetPawnPairs().isPresent()){
            throw new InvalidInputException();
        }else {
            //convention of input.targetPawnPairs ---> index 0 students from PlayerBoard/ index 1 students from Card
            int cont = 0;
            for (int i = 0; i < 6; i++) {
                if (this.students[i] == null) {
                    this.students[i] = input.getTargetPawnPairs().get()[0][cont]; //adding students from PlayerBoard's entrance into the card
                    cont++;
                    if (cont == 3) break;
                }
            }
            try { //adding student from Card to PlayerBoard's entrance
                input.getCaller().addStudentsToEntrance(new ArrayList<>(Arrays.asList(input.getTargetPawnPairs().get()[1]))); //todo check this statement
            } catch (FullEntranceException ex) {
                ex.printStackTrace();
            }
            addUse();
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
