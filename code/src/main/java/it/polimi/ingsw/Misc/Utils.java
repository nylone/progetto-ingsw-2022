package it.polimi.ingsw.Misc;

import it.polimi.ingsw.Exceptions.InvalidElementException;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.util.*;

import static it.polimi.ingsw.Constants.INPUT_NAME_TARGET_PAWN_PAIRS;

public class Utils {

    public static <T> void shuffle(List<T> coll) {
        Collections.shuffle(coll, new Random(System.currentTimeMillis()));
    }

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

    public static <T> T modularSelection(T startingElement, List<T> group, int movement) {
        int index = group.indexOf(startingElement);
        // todo add exception for element not in list
        return group.get((index + group.size() + movement) % group.size());
    }

    public static <T,C extends Integer> boolean canCollectionFit(Map<T, C> originalSet, Map<T, C> possibleSubset) {
        for (Map.Entry<T, C> entry: possibleSubset.entrySet()) {
            T colour = entry.getKey();
            int count = entry.getValue();
            if ( (int) originalSet.get(colour) - count < 0) {
                return false;
            }
        }
        return true;
    }
}