package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;

import java.util.ArrayList;
import java.util.Arrays;

public class Card01 extends StatefulEffect{
    private PawnColour[] students = new PawnColour[4];

    public Card01(GameBoard ctx){
        super(1,1, StateType.PAWNCOLOUR, ctx);
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

    public void addStudent(PawnColour p){ //add PawnColour into first empty position
        for(int i=0; i<4; i++){
            if(this.students[i] == null){
                this.students[i] = p;
                break;
            }
        }
    }

    public PawnColour getStudent(int i){
        if(0 <= i && i < 4) {
            PawnColour student = this.students[i];
            this.students[i] = null;
            return student;
        }
        else throw new InvalidInputException();
    }

    public void Use(CharacterCardInput input) {
        // todo raise exception when there's no island
        input.getTargetIsland().get().addStudent(input.getTargetPawn().get());
        if (timeUsed == 1) { this.cost++; }
        // todo refill the card with one student from the bag
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
