package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class Card11Test {
    final Model gb = new Model(GameMode.ADVANCED, "ari", "teo"); // advanced mode needed for character cards

    @Test
    public void checkUse() throws Exception {
        Card11 card11 = new Card11(gb);
        assertEquals(4, card11.getState().size()); // check if card has 4 items when initialized
        assertSame(card11.getStateType(), StateType.PAWNCOLOUR); // check that card contains students

        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();

        // creates input to let the current player interact with card
        CharacterCardInput input = new CharacterCardInput(pb);
        // selects third student on card
        input.setTargetPawn((PawnColour) card11.getState().get(2));
        PawnColour toAdd = input.getTargetPawn().get();

        // act
        // card should move student to dining room
        if (card11.checkInput(input).isEmpty()) card11.unsafeApplyEffect(input);

        assertEquals(1, pb.getDiningRoomCount(toAdd)); // one student should be added to the correct dining row
        assertEquals(4, card11.getState().size()); // checks if the card was filled up
    }

    @Test(expected = InputValidationException.class)
    public void checkExceptionInput() throws Exception {
        Card11 card11 = new Card11(gb);
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(pb);
        throw card11.checkInput(input).get();
    }

    @Test
    public void checkFullDiningRoom() throws Exception {
        Card11 card11 = new Card11(gb);
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();

        // creates input to let the current player interact with card
        CharacterCardInput input = new CharacterCardInput(pb);
        // selects third student on card
        input.setTargetPawn((PawnColour) card11.getState().get(2));
        // fills a dining room row
        for (int i = 0; i < 10; i++) {
            pb.unsafeAddStudentToDiningRoom(input.getTargetPawn().get());
        }
        // checks that card doesn't allow to add student to a full dining room row
        InputValidationException exception = assertThrows(InputValidationException.class, () -> {
            throw card11.checkInput(input).get();
        });

        // checks the specific exception error message
        assertEquals("An error occurred while validating: Dining Room\n" +
                "The error was: can't contain " + input.getTargetPawn().get() + "without overflowing.", exception.getMessage());
    }

    @Test
    public void EmptyStudentBagCardUse() throws Exception {
        Model g = new Model(GameMode.ADVANCED, "ari", "teo"); // advanced mode needed for character cards
        Card11 card = new Card11(g);
        // creates input to let the current player interact with card
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        // selects third student on card
        input.setTargetPawn((PawnColour) card.getState().get(2));
        // emptying student bag
        while (!g.getMutableStudentBag().isEmpty()) {
            g.getMutableStudentBag().extract();
        }
        // it should not be possible to activate card if student bag is empty
        InputValidationException e = card.checkInput(input).get();
        Assert.assertEquals("An error occurred while validating: Student Bag\n" +
                "The error was: is empty", e.getMessage());
    }

    @Test
    public void PawnNotPresentInCard() {
        Model g = new Model(GameMode.ADVANCED, "ari", "teo"); // advanced mode needed for character cards
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        Card11 card = new Card11(g);

        // selecting a student not contained in the card
        for (PawnColour p : PawnColour.values()) {
            if (!card.getState().contains(p)) {
                input.setTargetPawn(p);
                break;
            }
        }
        // it should not be possible to activate card if student is not on the card

        InputValidationException e = card.checkInput(input).get();

        Assert.assertEquals("An error occurred while validating: Target Pawn Colour\n" +
                "The error was: element Target Pawn Colour was found to be invalid (eg: null, out of bounds or otherwise incorrect).", e.getMessage());

    }

}
