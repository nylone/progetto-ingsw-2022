package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class Card07 extends StatefulEffect{
    private PawnColour[] students = new PawnColour[6];

    public Card07(GameBoard ctx){
        super(7,1,StateType.PAWNCOLOUR,ctx);
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
        return "Card01{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
