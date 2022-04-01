package it.polimi.ingsw.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    public static <T> T random(List<T> list) {
        Random random = new Random();
        int randomNumber = random.nextInt(list.size());
        return list.get(randomNumber);
    }

    public static <T> List<T> random(List<T> list, int size) {
        Random r = new Random();
        List<T> sublist = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            sublist.add(random(list));
        }
        return sublist;
    }

    public static <T> T modularSelection(T element, List<T> group, int movement) {
        int index = group.indexOf(element);
        // todo add exception for element not in list
        return group.get((index + movement) % group.size());
    }
}