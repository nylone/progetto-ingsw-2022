package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.toremove.FullDiningRoomException;
import it.polimi.ingsw.Exceptions.toremove.FullEntranceException;
import it.polimi.ingsw.Exceptions.toremove.InvalidInputException;

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

    public void Use(CharacterCardInput input) {
        //convention of input.targetPawnPairs ---> index 0 students from Entrance/ index 1 students from DiningRoom
        //assuming that students have already been removed from Entrance and DiningRoom when the input has been created
        if(!input.getTargetPawnPairs().isPresent()){
            throw new InvalidInputException();
        }else {
            if (input.getTargetPawnPairs().get()[0].length <= 2 && input.getTargetPawnPairs().get()[0].length <= 2 && input.getTargetPawnPairs().get()[0].length==input.getTargetPawnPairs().get()[1].length) {
                try {
                    input.getCaller().addStudentsToEntrance(new ArrayList<>(Arrays.asList(input.getTargetPawnPairs().get()[1])));
                } catch (FullEntranceException ex) {
                    ex.printStackTrace();
                }
                try {
                    for(int i=0; i<input.getTargetPawnPairs().get()[1].length; i++){
                        input.getCaller().addStudentToDiningRoom(input.getTargetPawnPairs().get()[0][i]);
                    }
                } catch (FullDiningRoomException ex) {
                    ex.printStackTrace();
                }
                addUse();
            }else{ throw new InvalidInputException("more than 2 pawns"); }
        }
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
