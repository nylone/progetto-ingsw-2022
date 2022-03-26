package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

public class IslandFieldTest extends TestCase {
    private IslandField field = new IslandField();

    @Test
    public void testValidIslandGroupById() {
        // arrange
        IslandField field = new IslandField();

        // act
        IslandGroup found = field.getIslandGroupById(1);

        // assert
        assertTrue(found.getId() == 1);

    }

    @Test
    public void testInvalidIslandGroupId() {
        // arrange
        IslandField field = new IslandField();

        try {
            // act
            IslandGroup found = field.getIslandGroupById(15);
            fail("An exception was thrown");
        } catch (InvalidInputException e) {
            // assert
            assertEquals("IslandGroup.id problem: id out of bound", e.getMessage());
        }
    }

    @Test
    public void testValidIslandId() {
        // act
        Island actualIsland = field.getIslandById(2);
        // assert
        assertTrue(actualIsland.getId() == 2);
    }

    @Test
    public void testInvalidIslandId() {
        // act
        try {
            field.getIslandById(18);
            fail("Exception was thrown");
        } catch (InvalidInputException e) {
            // assert
            assertEquals("Island.id problem: id out of bound", e.getMessage());
        }
    }
}