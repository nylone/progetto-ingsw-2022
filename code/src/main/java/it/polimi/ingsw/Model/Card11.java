package it.polimi.ingsw.Model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;

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
        for(int i=0; i<4; i++){
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

        this.cost++;
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
