package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

public class IslandFieldTest extends TestCase {

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
}