package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
/*
In setup, draw 4 Students and place them on this card.
 EFFECT: Take 1 Student from this card and place it on
an Island of your choice. Then, draw a new Student from the Bag and place it on this card. w
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
        if(!input.getTargetIsland().isPresent()||!input.getTargetPawn().isPresent()){
            throw new InvalidInputException();
        }else {
            input.getTargetIsland().get().addStudent(input.getTargetPawn().get());
            addUse();
            for(int i=0; i<4; i++){
                if(this.students[i]== null){
                    this.students[i] = context.getStudentBag().extract();
                    break;
                }
            }
        }
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
