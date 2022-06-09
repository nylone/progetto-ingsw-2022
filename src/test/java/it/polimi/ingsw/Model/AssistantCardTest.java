package it.polimi.ingsw.Model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AssistantCardTest {

    /**
     * Every {@link AssistantCard} movement should be proportional to its ID.
     * The formula is: maxMovement = round(ID/2)
     */
    @Test
    public void testGetMaxMovement() {
        // arrange
        AssistantCard assistantCard = new AssistantCard(5);

        // act
        int actual = assistantCard.getMaxMovement();

        // assert
        assertEquals(3, actual);
    }
}