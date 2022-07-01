package it.polimi.ingsw.Misc;

import it.polimi.ingsw.Model.Island;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
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

    @Test
    public void sortingElements() {
        // arrange
        Island i1 = new Island(1);
        Island i2 = new Island(2);
        Island i3 = new Island(3);
        List<Island> unorderedIslands = List.of(i3, i2, i1, i2, i1, i1);
        List<Island> expectedOrder = List.of(i1, i1, i1, i2, i2, i3);

        // act
        // sorts the initial array by frequency from highest to lowest
        ArrayList<Island> sorted = (ArrayList<Island>) Utils.sortByFrequency(unorderedIslands);

        // assert
        // checks all the elements are maintained
        assertEquals(sorted.size(), unorderedIslands.size());
        // checks that elements have been ordered correctly
        assertEquals(expectedOrder, sorted);
    }
}