package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;
import org.junit.Test;

import static org.junit.Assert.*;

public class Card01Test {

    @Test
    public void addingStudentUntilPossibleShouldWork() {
        // arrange
        GameBoard g = new GameBoard(GameMode.SIMPLE, "ari", "teo");
        Card01 card = new Card01(g);
        // act
        card.addStudent(PawnColour.BLUE);
        PawnColour actual = card.getStudent(0);
        // assert
        assertEquals(PawnColour.BLUE, actual);
    }

    @Test
    public void cardShouldContainFrom0To4Cards() {
        // arrange
        GameBoard g = new GameBoard(GameMode.SIMPLE, "ari", "teo");
        Card01 card = new Card01(g);
        // act
        try {
            card.getStudent(7);
            fail();
        }
        catch (InvalidInputException e) {
            assertEquals("Invalid input provided", e.getMessage());
        }
    }

    @Test
    public void usingEffectShouldAddStudentToIsland() {
        // arrange
        GameBoard g = new GameBoard(GameMode.SIMPLE, "ari", "teo");
        CharacterCardInput input = new CharacterCardInput(g.getPlayerBoardByNickname("ari"));
        Island island = g.getIslandField().getIslandById(3);
        int expected = island.getStudents().size();
        input.setTargetIsland(island);
        input.setTargetPawn(PawnColour.BLUE);
        Card01 card = new Card01(g);
        // act
        card.Use(input);
        // assert
        assertTrue(island.getStudents().size() == expected + 1);

    }



}