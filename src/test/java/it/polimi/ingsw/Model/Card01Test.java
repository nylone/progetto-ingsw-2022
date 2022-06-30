package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;
import org.junit.Assert;
import org.junit.Test;

import java.util.EnumMap;

import static org.junit.Assert.*;

/**
 * This test set verifies that character card 01 is able to hold, move and identify students.
 * <br>
 * It should hold only 4 students, it should move students only to an island, and it should identify which
 * students it currently holds to error out if somebody tries to access a student which the card doesn't manage
 */
public class Card01Test {

    /**
     * Character card 01 should always hold 4 students, it should do so by refilling itself from the
     * student bag when it gets used (and loses 1 student)
     *
     * @throws Exception is thrown when an invalid card activation happens, but this test should not error out
     *                   (by definition)
     */
    @Test
    public void cardShouldAlwaysHave4Students() throws Exception {
        // arrange
        Model g = new Model(GameMode.ADVANCED, "ari", "teo"); // advanced mode needed for character cards
        Card01 card = new Card01(g);
        // creates input to let current player interact with card
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        // input holds the selected student to be moved from the card
        input.setTargetPawn((PawnColour) card.getState().get(1));
        // input holds the destination island
        input.setTargetIsland(Utils.random(g.getMutableIslandField().getMutableGroups()).getMutableIslands().get(0));
        int initialSize = card.getState().size();

        // act
        // activates the card to move the student to the island
        if (card.checkInput(input).isEmpty()) card.unsafeApplyEffect(input);

        // assert
        assertEquals(4, initialSize); // card should be initialized with 4 students
        assertEquals(4, card.getState().size()); // card should be refilled and still hold 4 students
        assertSame(card.getStateType(), StateType.PAWNCOLOUR); // every student should be a PawnColour class instance
    }

    /**
     * Character card 01 should move a student to the chosen island
     *
     * @throws Exception is thrown when an invalid card activation happens, but this test should not error out
     *                   (by definition)
     */
    @Test
    public void usingEffectShouldAddStudentToIsland() throws Exception {
        // arrange
        Model g = new Model(GameMode.ADVANCED, "ari", "teo"); // advanced mode needed for character cards
        Card01 card = new Card01(g);
        // creates input to let current player interact with card
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        // chooses a random island for the student to be moved to
        Island island = Utils.random(g.getMutableIslandField().getMutableGroups()).getMutableIslands().get(0);
        int expected = island.getStudents().size(); // initial size of students on island
        input.setTargetIsland(island); // input holds the destination island
        // input holds the selected student to be moved from the card
        input.setTargetPawn((PawnColour) card.getState().get(1));

        // act
        // activates the card to move the student to the island
        if (card.checkInput(input).isEmpty()) card.unsafeApplyEffect(input);

        // assert
        // the size of students on island should be incremented by one
        assertEquals(island.getStudents().size(), expected + 1);
        // the chosen island should contain the chosen student from the card
        assertTrue(island.getStudents().contains(input.getTargetPawn().get()));
    }

    /**
     * An invalid player action should error out.
     * <br>
     * The island could be wrong, the selected student could be not hold by the card, or the input object
     * could be constructed wrongly in some other ways.
     */
    @Test
    public void NoIslandException() {
        // arrange
        Model g = new Model(GameMode.ADVANCED, "ari", "teo");
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        Card01 card = new Card01(g);

        // act
            InputValidationException e = card.checkInput(input).get();

            Assert.assertEquals("An error occurred while validating: Target Island\n" +
                    "The error was: element Target Island was found to be invalid (eg: null, out of bounds or otherwise incorrect).", e.getMessage());
    }

    @Test
    public void EmptyPawnException() throws Exception {
        Model g = new Model(GameMode.ADVANCED, "ari", "teo");
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        Card01 card = new Card01(g);
        input.setTargetIsland(g.getMutableIslandField().getMutableIslandById(0));
            InputValidationException e = card.checkInput(input).get();
            Assert.assertEquals("An error occurred while validating: Target Pawn Colour\n" +
                    "The error was: element Target Pawn Colour was found to be invalid (eg: null, out of bounds or otherwise incorrect).", e.getMessage());
    }

    @Test
    public void PawnNotPresentInCard() throws Exception {
        Model g = new Model(GameMode.ADVANCED, "ari", "teo");
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        Card01 card = new Card01(g);
        input.setTargetIsland(g.getMutableIslandField().getMutableIslandById(0));
        EnumMap<PawnColour, Integer> pawnColourIntegerEnumMap = new EnumMap<>(PawnColour.class);
        for (PawnColour p : card.getState().stream().map(o -> (PawnColour) o).toList()) {
            pawnColourIntegerEnumMap.merge(p, 1, Integer::sum);
        }
        for (PawnColour p : PawnColour.values()) {
            if (!pawnColourIntegerEnumMap.containsKey(p)) {
                input.setTargetPawn(p);
                break;
            }
        }
            InputValidationException e = card.checkInput(input).get();
            Assert.assertEquals("An error occurred while validating: Target Pawn Colour\n" +
                    "The error was: element Target Pawn Colour was found to be invalid (eg: null, out of bounds or otherwise incorrect).", e.getMessage());
    }

    @Test(expected = InputValidationException.class)
    public void checkInvalidIslandInput() throws InputValidationException {
        Model gb = new Model(GameMode.ADVANCED, "ari", "teo"); // advanced mode needed for character cards
        Card01 card = new Card01(gb);
        // creates a wrong input (player will select an invalid island)
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        Island island = new Island(13);
        input.setTargetPawn((PawnColour) card.getState().get(0));
        input.setTargetIsland(island);
        throw card.checkInput(input).get();
    }

    @Test(expected = InputValidationException.class)
    public void checkIslandNotInField() throws Exception {
        Model gb = new Model(GameMode.ADVANCED, "ari", "teo"); // advanced mode needed for character cards
        Card05 card05 = new Card05(gb);
        // creates a wrong input (player will select an invalid island)
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        Island island = new Island(8);
        input.setTargetIsland(island);
        throw card05.checkInput(input).get();
    }

    @Test
    public void EmptyStudentBagCardUse() throws Exception {
        Model g = new Model(GameMode.ADVANCED, "ari", "teo");
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        // selects the first and second students from both the card and the entrance and links them together
        Card01 card = new Card01(g);
        input.setTargetIsland(Utils.random(g.getMutableIslandField().getMutableGroups()).getMutableIslands().get(0));
        input.setTargetPawn((PawnColour) card.getState().get(1));
        while (!g.getMutableStudentBag().isEmpty()) {
            g.getMutableStudentBag().extract();
        }
            InputValidationException e = card.checkInput(input).get();
            Assert.assertEquals("An error occurred while validating: Student Bag\n" +
                    "The error was: is empty", e.getMessage());
    }

    @Test
    public void EmptyStudentBagExceptionCardConstructor() {
        Model g = new Model(GameMode.ADVANCED, "ari", "teo");
        //create a new Card until student bag has 5 students or fewer
        do {
            new Card01(g);
        } while (g.getMutableStudentBag().getSize() >= 4);
        try {
            new Card01(g);
        } catch (RuntimeException e) {
            assertEquals("it.polimi.ingsw.Exceptions.Container.EmptyContainerException: An error occurred on: StudentBag\n" +
                    "The error was: StudentBag was found empty.", e.getMessage());
        }
    }

}