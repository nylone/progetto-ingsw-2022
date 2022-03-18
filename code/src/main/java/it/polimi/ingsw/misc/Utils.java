package it.polimi.ingsw.misc;

import java.util.ArrayList;
import java.util.Random;

public class Utils {

    public static <T> T random(ArrayList<T> list) {

        Random random = new Random();

        int randomNumber = random.nextInt(list.size());
        return list.get(randomNumber);

    }
}