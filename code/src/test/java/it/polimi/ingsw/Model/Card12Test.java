package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.FullDiningRoomException;
import it.polimi.ingsw.Exceptions.InvalidInputException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class Card12Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "rouge", "marianna");
    Card12 card = new Card12(gb);
    @Test
    public void checkUse() throws FullDiningRoomException {
        PlayerBoard pb = gb.getPlayerBoardByNickname("marianna");
        PlayerBoard pb2 = gb.getPlayerBoardByNickname("rouge");
        CharacterCardInput input = new CharacterCardInput(pb2);
        for(int i=0; i<=4;i++) pb.addStudentToDiningRoom(PawnColour.RED);
        pb2.addStudentToDiningRoom(PawnColour.RED);
        input.setTargetPawn(PawnColour.RED);
        card.Use(input);

        assertTrue(pb.getDiningRoomCount(PawnColour.RED)==2);
        assertTrue(pb2.getDiningRoomCount(PawnColour.RED)==0);
    }

    @Test(expected = InvalidInputException.class)
    public void checkEmptyInput(){
        PlayerBoard pb2 = gb.getPlayerBoardByNickname("rouge");
        CharacterCardInput input = new CharacterCardInput(pb2);
        card.Use(input);
    }
}
