package it.polimi.ingsw.Model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class IslandGroupTest {

    @Test
    public void testGetStudentCount() {
        // arrange
        Island island = new Island(4);
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.YELLOW);
        IslandGroup islandGroup = new IslandGroup(island);
        // act
        int expectedBlue = islandGroup.getStudentCount().get(PawnColour.BLUE);
        int expectedYellow = islandGroup.getStudentCount().get(PawnColour.YELLOW);
        // assert
        assertTrue(expectedBlue == 2);
        assertTrue(expectedYellow == 1);
    }

    @Test
    public void testGetStudents() {
        // arrange
        Island i1 = new Island(7);
        Island i2 = new Island(3);
        i1.addStudent(PawnColour.BLUE);
        i1.addStudent(PawnColour.RED);
        i2.addStudent(PawnColour.GREEN);
        IslandGroup islandGroup1 = new IslandGroup(i1);
        IslandGroup islandGroup2 = new IslandGroup(i2);
        IslandGroup islandGroup = new IslandGroup(islandGroup1, islandGroup2);
        ArrayList<PawnColour> expected = new ArrayList<>(Arrays.asList(PawnColour.BLUE, PawnColour.RED, PawnColour.GREEN));
        // act
        ArrayList<PawnColour> actual = islandGroup.getStudents();
        // assert
        assertEquals(expected, actual);
        assertTrue(actual.size() == 3);

    }
}