package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PlayerBoardTest {

    @Test
    public void sizeIncreasedAfterAddingStudentToDiningRoom() throws FullContainerException {
        // arrange
        PlayerBoard playerBoard = new PlayerBoard(2, 3, "ari", new StudentBag(20));
        int initial = playerBoard.getDiningRoomCount(PawnColour.RED);
        // act
        playerBoard.unsafeAddStudentToDiningRoom(PawnColour.RED);
        // assert
        assertEquals(playerBoard.getDiningRoomCount(PawnColour.RED), initial + 1);
    }

    @Test
    public void fullDiningRoomShouldRaiseException() throws FullContainerException {
        // arrange
        PlayerBoard playerBoard = new PlayerBoard(1, 2, "alessandro", new StudentBag(50));
        // fills yellow dining room row completely
        for (int i = 0; i < 10; i++) {
            playerBoard.unsafeAddStudentToDiningRoom(PawnColour.YELLOW);
        }
        try {
            // act
            // tempting to add one more yellow student to full row
            playerBoard.unsafeAddStudentToDiningRoom(PawnColour.YELLOW);
            fail();
        } catch (FullContainerException e) {
            // assert
            assertEquals("An error occurred on: DiningRoom\nThe error was: DiningRoom was found full.", e.getMessage());
        }
    }

    @Test
    public void sizeDecreasedAfterRemovingStudent() throws FullContainerException, EmptyContainerException {
        // arrange
        PlayerBoard playerBoard = new PlayerBoard(3, 3, "ale", new StudentBag(30));
        playerBoard.unsafeAddStudentToDiningRoom(PawnColour.BLUE);
        playerBoard.unsafeAddStudentToDiningRoom(PawnColour.BLUE);
        int initial = playerBoard.getDiningRoomCount(PawnColour.BLUE);
        // act
        playerBoard.unsafeRemoveStudentFromDiningRoom(PawnColour.BLUE);
        playerBoard.unsafeRemoveStudentFromDiningRoom(PawnColour.BLUE);
        // assert
        assertEquals(playerBoard.getDiningRoomCount(PawnColour.BLUE), initial - 2);
    }

    @Test(expected = EmptyContainerException.class)
    public void removeStudentFromEmptyRow() throws EmptyContainerException {
        PlayerBoard playerBoard = new PlayerBoard(3, 3, "ale", new StudentBag(30));
        // trying to remove a red student when there are none in the row
        playerBoard.unsafeRemoveStudentFromDiningRoom(PawnColour.RED);
    }


    @Test
    public void fullEntranceException() {
        // arrange
        PlayerBoard playerBoard = new PlayerBoard(2, 4, "teo", new StudentBag(50));
        PawnColour expected = PawnColour.YELLOW;
        try {
            // act
            // trying to add one more student when entrance is full (at the beginning of the game)
            playerBoard.addStudentToEntrance(expected);
        } catch (FullContainerException e) {
            // assert
            assertEquals("An error occurred on: Entrance\n" +
                    "The error was: Entrance was found full.", e.getMessage());
        }
    }

    @Test
    public void invalidEntrancePositionException() throws Exception {
        PlayerBoard playerBoard = new PlayerBoard(2, 4, "teo", new StudentBag(24));
        playerBoard.removeStudentFromEntrance(0);
        try {
            // act
            // trying to remove a student already removed
            playerBoard.removeStudentFromEntrance(0);
        } catch (InvalidContainerIndexException exception) {
            assertEquals("An error occurred on: Entrance\n" +
                    "The error was: provided index is out of bounds or no valid value could be retrieved.", exception.getMessage());
        }
    }
}