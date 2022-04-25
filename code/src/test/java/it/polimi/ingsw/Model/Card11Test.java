package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Operation.FailedOperationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.*;

public class Card11Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card11 card11 = new Card11(gb);
    PlayerBoard pb = new PlayerBoard(1, 2, "ari", gb.getMutableStudentBag());

    @Test
    public void checkUse() throws Exception {
        assertTrue(card11.getState().size()==4);

        CharacterCardInput input = new CharacterCardInput(pb);
        input.setTargetPawn((PawnColour) card11.getState().get(2));
        PawnColour toAdd = input.getTargetPawn().get();
        assertTrue(card11.getState().size()==4);
        card11.unsafeApplyEffect(input);
        assertTrue(pb.getDiningRoomCount(toAdd)==1);
        assertTrue(card11.getState().size()==4);
    }
    @Test(expected = FailedOperationException.class)
    public void checkExceptionInput() throws Exception {
        CharacterCardInput input = new CharacterCardInput(pb);
        card11.unsafeApplyEffect(input);
    }
    @Test
    public void checkFullDiningRoom() throws Exception {
        CharacterCardInput input = new CharacterCardInput(pb);
        input.setTargetPawn((PawnColour) card11.getState().get(2));
        for(int i=0; i<10; i++){
            pb.addStudentToDiningRoom(input.getTargetPawn().get());
        }
        FailedOperationException exception = assertThrows(FailedOperationException.class, () -> {
            card11.unsafeApplyEffect(input);
        });
        assertEquals("An error occurred while running the following operation: [MODEL] Card011 unsafeApplyEffect" +
                "\nThe error was: could critically failed during execution." +
                "\nAdditional INFO: Target pawn was not contained in card's state", exception.getMessage());
    }

}
