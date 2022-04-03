package it.polimi.ingsw.Model;

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

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public int getTimeUsed() {
        return timeUsed;
    }


    public ArrayList<Object> getState() {
        return new ArrayList<>(Arrays.asList(students));
    }

    public StateType getStateType() {
        return stateType;
    }

    public void Use(CharacterCardInput input) {
        //todo
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
