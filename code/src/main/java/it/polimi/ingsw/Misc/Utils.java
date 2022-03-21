package it.polimi.ingsw.Misc;

import java.util.List;
import java.util.Random;

public class Utils {

    public static <T> T random(List<T> list) {
        Random random = new Random();

        int randomNumber = random.nextInt(list.size());
        return list.get(randomNumber);
    }

    public static <T> T modularSelection(T element, List<T> group, int movement){
        int index = group.indexOf(element);
        return group.get((index + movement)%group.size());
    }
}