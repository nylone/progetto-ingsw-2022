package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class Card01 extends StatefulEffect{
    private PawnColour[] students = new PawnColour[4];

    public Card01(GameBoard ctx){
        super(1,1,StateType.PAWNCOLOUR,ctx);
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

    public PawnColour getStudent(int i){ //todo assure 0<= i< 4
        PawnColour student = this.students[i];
        this.students[i] = null;
        return student;

    }

    public void Use(CharacterCardInput input) {
        //todo
        input.getTargetIsland().get().addStudent(input.getTargetPawn().get());
        this.cost++;
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
