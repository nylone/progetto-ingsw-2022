package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.FullDiningRoomException;
import it.polimi.ingsw.Exceptions.FullEntranceException;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;

/*
EFFECT: You may exchange up to 2 Students between your entrance and your Dining Room
 */
public class Card10 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 112L; // convention: 1 for model, (01 -> 99) for objects

    public Card10(GameBoard ctx) {
        super(10, 1, ctx);
    }

    public void Use(CharacterCardInput input) { //todo: can be written better
        //convention of input.targetPawnPairs ---> index 0 students from Entrance/ index 1 students from DiningRoom
        //assuming that students have already been removed from Entrance and DiningRoom when the input has been created
        try {
            input.getCaller().addStudentsToEntrance(new ArrayList<>(Arrays.asList(input.getTargetPawnPairs().get()[0][0])));
            input.getCaller().addStudentsToEntrance(new ArrayList<>(Arrays.asList(input.getTargetPawnPairs().get()[0][1])));
        }catch(FullEntranceException ex){
            ex.printStackTrace();
        }
        try {
            input.getCaller().addStudentToDiningRoom(input.getTargetPawnPairs().get()[1][0]);
            input.getCaller().addStudentToDiningRoom(input.getTargetPawnPairs().get()[1][1]);
        }catch(FullDiningRoomException ex){
            ex.printStackTrace();
        }
        addUse();
    }

    //test purpose only
    @Override
    public String toString() {
        return "Card10{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
