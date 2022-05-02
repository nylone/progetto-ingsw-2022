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
        ArrayList<String> list = new ArrayList<String>(3);
        list.addAll(Arrays.asList("mago", "strega", "re"));

        // execution of the random method
        String randomValue = Utils.random(list);

        System.out.println(randomValue);

        // verify correctness of random implementation
        assertTrue(list.contains(randomValue));

    }

    @Test
    public void randomIntTest() {

        // initialized ArrayList of Integers, to check if the method works with a different parameter type
        ArrayList<Integer> list = new ArrayList<Integer>(4);
        Collections.addAll(list, 1, 5, 10, 13);

        int generatedNumber = Utils.random(list);

        System.out.println(generatedNumber);

        assertTrue(list.contains(generatedNumber));

    }

}