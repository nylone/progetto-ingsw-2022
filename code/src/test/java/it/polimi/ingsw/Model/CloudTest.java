package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.NoPawnInCloudException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CloudTest {

    // testing extractContents method
    @Test
    public void emptyCloudShouldRaiseException() {
        Cloud cloud = new Cloud(4);
        try {
            cloud.extractContents();
            fail("Extracting from empty cloud");
        }
        catch(NoPawnInCloudException e) {
            assertEquals("Cloud is empty", e.getMessage());
        }
    }

    @Test
    public void cloudShouldBeEmptied() throws NoPawnInCloudException {
        // arrange
        Cloud cloud = new Cloud(2);
        ArrayList<PawnColour> colours = new ArrayList<>(3);
        colours.add(PawnColour.RED);
        colours.add(PawnColour.BLUE);
        colours.add(PawnColour.GREEN);
        cloud.fill(colours);
        // act
        cloud.extractContents();
        // assert
        assertTrue(cloud.getContents().isEmpty());
    }

    @Test
    public void shouldReturnCorrectStudents() throws NoPawnInCloudException{
        // arrange
        Cloud cloud = new Cloud(2);
        ArrayList<PawnColour> expected = new ArrayList<>(3);
        expected.add(PawnColour.RED);
        expected.add(PawnColour.BLUE);
        expected.add(PawnColour.GREEN);
        cloud.fill(expected);
        // act
        ArrayList<PawnColour> actual = cloud.extractContents();
        // assert
        assertEquals(expected, actual);

    }

    // testing fill method
    @Test
    public void checkCloudHasBeenFilled() throws NoPawnInCloudException {
        // arrange
        Cloud cloud = new Cloud(1);
        ArrayList<PawnColour> colours = new ArrayList<>(3);
        colours.add(PawnColour.RED);
        colours.add(PawnColour.BLUE);
        colours.add(PawnColour.GREEN);
        // act
        cloud.fill(colours);
        // assert
        assertTrue(cloud.getContents().equals(colours));
    }

    @Test
    public void filledCloudShouldRaiseException() throws NoPawnInCloudException {
        Cloud cloud = new Cloud(3);
        ArrayList<PawnColour> colours = new ArrayList<>(3);
        colours.add(PawnColour.RED);
        colours.add(PawnColour.BLUE);
        colours.add(PawnColour.GREEN);
        cloud.fill(colours);
        try {
            cloud.fill(colours);
            fail("Cloud already filled");
        }
        catch (NoPawnInCloudException e) {
            assertEquals("Cloud is empty", e.getMessage());
        }
    }
}