package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;
import org.junit.Test;

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
                "The error was: DiningRoomcan't contain " + input.getTargetPawn().get() + "without overflowing.", exception.getMessage());
    }

}
