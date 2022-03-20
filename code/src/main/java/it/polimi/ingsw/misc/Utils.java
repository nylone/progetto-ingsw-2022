package it.polimi.ingsw.misc;

import it.polimi.ingsw.model.IslandGroup;

import java.util.ArrayList;
import java.util.Random;

public class Utils {

    public static <T> T random(ArrayList<T> list) {

        Random random = new Random();

        int randomNumber = random.nextInt(list.size());
        return list.get(randomNumber);
    }


    public static <T> T modularSelection(T element, ArrayList<T> group, int movement){
        int index = group.indexOf(element);

        int nextPosition = modularSum(index, movement);

        return group.get(nextPosition);
    }

    public static int modularSum(int currentPos, int movement){

        int sum = currentPos + movement;
        if (sum > 12) {
            sum = sum - 12;
        }
        return sum;
    }



}