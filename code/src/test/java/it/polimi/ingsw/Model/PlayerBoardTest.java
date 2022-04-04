package it.polimi.ingsw.Model;


import it.polimi.ingsw.Exceptions.FullDiningRoomException;
import it.polimi.ingsw.Exceptions.FullEntranceException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

public class PlayerBoardTest {

    @Test
    public void sizeIncreasedAfterAddingStudentToDiningRoom() throws FullDiningRoomException {
        // arrange
        PlayerBoard playerBoard = new PlayerBoard(2, 3, "ari", new StudentBag(20));
        int expected = playerBoard.getDiningRoomCount(PawnColour.RED);
        // act
        playerBoard.addStudentToDiningRoom(PawnColour.RED);
        // assert
        assertTrue(playerBoard.getDiningRoomCount(PawnColour.RED) == expected + 1);
    }

    @Test
    public void FullDiningRoomShouldRaiseException() throws FullDiningRoomException {
        // arrange
        PlayerBoard playerBoard = new PlayerBoard(1, 2, "alessandro", new StudentBag(50));
        for (int i = 0; i < 10; i++) {
            playerBoard.addStudentToDiningRoom(PawnColour.YELLOW);
        }
        try {
            playerBoard.addStudentToDiningRoom(PawnColour.YELLOW);
            fail();
        }
        catch (FullDiningRoomException e) {
            assertEquals("No more space for that student in dining room", e.getMessage());
        }
    }

    @Test
    public void sizeDecreasedAfterRemovingStudent() {
        // arrange
        PlayerBoard playerBoard = new PlayerBoard(3, 3, "ale", new StudentBag(30));
        int expected = playerBoard.getDiningRoomCount(PawnColour.BLUE);
        // act
        playerBoard.removeStudentFromDiningRoom(PawnColour.BLUE, 2);
        // assert
        assertTrue(playerBoard.getDiningRoomCount(PawnColour.BLUE) == expected - 2);
    }

    @Test
    public void sizeIncreasedAfterAddingStudentToEntrance() {
        // arrange
        PlayerBoard playerBoard = new PlayerBoard(2, 4, "teo", new StudentBag(50));
        ArrayList<PawnColour> expected = new ArrayList<>();
        expected.add(PawnColour.YELLOW);
        expected.add(PawnColour.BLUE);
        // act
        try {
            playerBoard.addStudentsToEntrance(expected);
            fail();
        }
        catch(FullEntranceException e) {
            // assert
            assertEquals("No more space in entrance", e.getMessage());

        }
    }
}