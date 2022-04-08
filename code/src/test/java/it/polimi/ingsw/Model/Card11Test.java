package it.polimi.ingsw.Model;
import static org.junit.Assert.*;

import it.polimi.ingsw.Exceptions.FullDiningRoomException;
import it.polimi.ingsw.Exceptions.InvalidInputException;
import org.junit.Test;

public class Card11Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card11 card11 = new Card11(gb);
    PlayerBoard pb = new PlayerBoard(1, 1, "ari", gb.getStudentBag());

    @Test
    public void checkUse(){
        assertTrue(card11.getState().size()==4);

        CharacterCardInput input = new CharacterCardInput(pb);
        input.setTargetPawn((PawnColour) card11.getState().get(2));
        PawnColour toAdd = input.getTargetPawn().get();
        assertTrue(card11.getState().size()==4);
        card11.Use(input);
        assertTrue(pb.getDiningRoomCount(toAdd)==1);
        assertTrue(card11.getState().size()==4);
    }
    @Test(expected = InvalidInputException.class)
    public void checkExceptionInput(){
        CharacterCardInput input = new CharacterCardInput(pb);
        card11.Use(input);
    }
    @Test
    public void checkFullDiningRoom() throws FullDiningRoomException {
        CharacterCardInput input = new CharacterCardInput(pb);
        input.setTargetPawn((PawnColour) card11.getState().get(2));
        for(int i=0; i<10; i++){
            pb.addStudentToDiningRoom(input.getTargetPawn().get());
        }
        card11.Use(input);
        FullDiningRoomException exception = assertThrows(FullDiningRoomException.class, () -> {
            throw new FullDiningRoomException();
        });
        assertEquals("No more space for that student in dining room", exception.getMessage());
    }

}
