package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PlayerBoardTest {

    @Test
    public void sizeIncreasedAfterAddingStudentToDiningRoom() throws FullContainerException {
        // arrange
        PlayerBoard playerBoard = new PlayerBoard(2, 3, "ari", new StudentBag(20));
        int expected = playerBoard.getDiningRoomCount(PawnColour.RED);
        // act
        playerBoard.addStudentToDiningRoom(PawnColour.RED);
        // assert
        assertEquals(playerBoard.getDiningRoomCount(PawnColour.RED), expected + 1);
    }

    @Test
    public void FullDiningRoomShouldRaiseException() throws FullContainerException {
        // arrange
        PlayerBoard playerBoard = new PlayerBoard(1, 2, "alessandro", new StudentBag(50));
        for (int i = 0; i < 10; i++) {
            playerBoard.addStudentToDiningRoom(PawnColour.YELLOW);
        }
        try {
            playerBoard.addStudentToDiningRoom(PawnColour.YELLOW);
            fail();
        } catch (FullContainerException e) {
            assertEquals("An error occurred on: DiningRoom\nThe error was: DiningRoom was found full.", e.getMessage());
        }
    }

    @Test
    public void sizeDecreasedAfterRemovingStudent() throws FullContainerException, EmptyContainerException {
        // arrange
        PlayerBoard playerBoard = new PlayerBoard(3, 3, "ale", new StudentBag(30));
        playerBoard.addStudentToDiningRoom(PawnColour.BLUE);
        playerBoard.addStudentToDiningRoom(PawnColour.BLUE);
        int expected = playerBoard.getDiningRoomCount(PawnColour.BLUE);
        // act
        playerBoard.removeStudentsFromDiningRoom(PawnColour.BLUE, 2);
        // assert
        assertEquals(playerBoard.getDiningRoomCount(PawnColour.BLUE), expected - 2);
    }

    @Test(expected = EmptyContainerException.class)
    public void removeStudentException() throws EmptyContainerException {
        PlayerBoard playerBoard = new PlayerBoard(3, 3, "ale", new StudentBag(30));
        playerBoard.removeStudentsFromDiningRoom(PawnColour.RED, 1);
    }


    @Test
    public void FullEntranceException() {
        // arrange
        PlayerBoard playerBoard = new PlayerBoard(2, 4, "teo", new StudentBag(50));
        ArrayList<PawnColour> expected = new ArrayList<>();
        expected.add(PawnColour.YELLOW);
        expected.add(PawnColour.BLUE);
        // act
        try {
            playerBoard.addStudentsToEntrance(expected);
        } catch (FullContainerException e) {
            // assert
            assertEquals("An error occurred on: Entrance\n" +
                    "The error was: Entrance was found full.", e.getMessage());
        }
    }

    @Test
    public void InvalidEntrancePositionException() throws Exception {
        PlayerBoard playerBoard = new PlayerBoard(2, 4, "teo", new StudentBag(24));
        playerBoard.removeStudentFromEntrance(0);
        try {
            playerBoard.removeStudentFromEntrance(0);
        } catch (InvalidContainerIndexException exception) {
            assertEquals("An error occurred on: Entrance\n" +
                    "The error was: provided index is out of bounds or no valid value could be retrieved.", exception.getMessage());
        }
    }
}