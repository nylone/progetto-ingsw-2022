package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;
import org.junit.Assert;
import org.junit.Test;

import java.util.EnumMap;

import static org.junit.Assert.*;

public class Card11Test {
    Model gb = new Model(GameMode.ADVANCED, "ari", "teo");

    @Test
    public void checkUse() throws Exception {
        Card11 card11 = new Card11(gb);
        assertEquals(4, card11.getState().size());
        assertSame(card11.getStateType(), StateType.PAWNCOLOUR);

        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();

        CharacterCardInput input = new CharacterCardInput(pb);
        input.setTargetPawn((PawnColour) card11.getState().get(2));
        PawnColour toAdd = input.getTargetPawn().get();
        assertEquals(4, card11.getState().size());
        if (card11.checkInput(input)) card11.unsafeApplyEffect(input);
        assertEquals(1, pb.getDiningRoomCount(toAdd));
        assertEquals(4, card11.getState().size());
    }

    @Test(expected = InputValidationException.class)
    public void checkExceptionInput() throws Exception {
        Card11 card11 = new Card11(gb);
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        CharacterCardInput input = new CharacterCardInput(pb);
        if (card11.checkInput(input)) card11.unsafeApplyEffect(input);
    }

    @Test
    public void checkFullDiningRoom() throws Exception {
        Card11 card11 = new Card11(gb);
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();

        CharacterCardInput input = new CharacterCardInput(pb);
        input.setTargetPawn((PawnColour) card11.getState().get(2));
        for (int i = 0; i < 10; i++) {
            pb.unsafeAddStudentToDiningRoom(input.getTargetPawn().get());
        }
        InputValidationException exception = assertThrows(InputValidationException.class, () -> {
            if (card11.checkInput(input)) card11.unsafeApplyEffect(input);
        });

        assertEquals("An error occurred while validating: DiningRoom\n" +
                "The error was: DiningRoom can't contain " + input.getTargetPawn().get() + "without overflowing.", exception.getMessage());
    }

    @Test
    public void EmptyStudentBagExceptionCardConstructor() {
        Model g = new Model(GameMode.ADVANCED, "ari", "teo");
        // creates a wrong input which will not be filled with information
        Card11 card;
        //create a new Card until student bag has 5 students or fewer
        do {
            card = new Card11(g);
        } while (g.getMutableStudentBag().getSize() >= 4);
        try {
            card = new Card11(g);
        } catch (RuntimeException e) {
            assertEquals("it.polimi.ingsw.Exceptions.Container.EmptyContainerException: An error occurred on: StudentBag\n" +
                    "The error was: StudentBag was found empty.", e.getMessage());
        }
    }

    @Test
    public void EmptyStudentBagCardUse() throws Exception {
        Model g = new Model(GameMode.ADVANCED, "ari", "teo");
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        // selects the first and second students from both the card and the entrance and links them together
        Card11 card = new Card11(g);
        input.setTargetPawn((PawnColour) card.getState().get(2));
        while (!g.getMutableStudentBag().isEmpty()) {
            g.getMutableStudentBag().extract();
        }
        try {
            card.checkInput(input);
        } catch (GenericInputValidationException e) {
            Assert.assertEquals("An error occurred while validating: Student Bag\n" +
                    "The error was: Student Bag is empty", e.getMessage());
        }
    }

    @Test
    public void PawnNotPresentInCard() throws Exception {
        Model g = new Model(GameMode.ADVANCED, "ari", "teo");
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        Card11 card = new Card11(g);
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
        try {
            card.checkInput(input);
        } catch (InvalidElementException e) {
            Assert.assertEquals("An error occurred while validating: Target Pawn Colour\n" +
                    "The error was: element Target Pawn Colour was found to be invalid (eg: null, out of bounds or otherwise incorrect).", e.getMessage());
        }
    }

}
