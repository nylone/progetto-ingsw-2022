package it.polimi.ingsw.Model;


import it.polimi.ingsw.Exceptions.FullDiningRoomException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PlayerBoardTest {

    @Test
    public void sizeIncreasedAfterAddingStudents() throws FullDiningRoomException {
        // arrange
        PlayerBoard playerBoard = new PlayerBoard(2, 3, "ari", new StudentBag(20));
        int expected = playerBoard.getDiningRoomCount(PawnColour.RED);
        // act
        playerBoard.addStudentToDiningRoom(PawnColour.RED);
        // assert
        assertTrue(playerBoard.getDiningRoomCount(PawnColour.RED) == expected + 1);
    }
}