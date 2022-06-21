package it.polimi.ingsw.Misc;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class UtilsTest {

    @Test
    public void randomStringTest() {

        // initialized arrayList to apply the "random" method on it
        ArrayList<String> list = new ArrayList<>(3);
        list.addAll(Arrays.asList("mago", "strega", "re"));

        // execution of the random method
        String randomValue = Utils.random(list);

        // verify correctness of random implementation
        assertTrue(list.contains(randomValue));

    }

    @Test
    public void randomIntTest() {

        // initialized ArrayList of Integers, to check if the method works with a different parameter type
        ArrayList<Integer> list = new ArrayList<>(4);
        Collections.addAll(list, 1, 5, 10, 13);

        int generatedNumber = Utils.random(list);

        assertTrue(list.contains(generatedNumber));

    }

}