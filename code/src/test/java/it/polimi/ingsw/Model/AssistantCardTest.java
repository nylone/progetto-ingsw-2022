package it.polimi.ingsw.Model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AssistantCardTest {
    @Test
    public void testGetMaxMovement() {
        // arrange
        AssistantCard assistantCard = new AssistantCard(5);

        // act
        int actual = assistantCard.getMaxMovement();

        // assert
        assertTrue( actual == 3);
    }
}