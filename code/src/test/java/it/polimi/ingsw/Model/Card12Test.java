package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class Card12Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "rouge", "marianna");
    Card12 card = new Card12(gb);
    @Test
    public void checkUse() throws Exception {
        PlayerBoard pb = gb.getMutablePlayerBoardByNickname("marianna");
        PlayerBoard pb2 = gb.getMutablePlayerBoardByNickname("rouge");
        CharacterCardInput input = new CharacterCardInput(pb2);
        for(int i=0; i<=4;i++) pb.addStudentToDiningRoom(PawnColour.RED);
        pb2.addStudentToDiningRoom(PawnColour.RED);
        input.setTargetPawn(PawnColour.RED);
        card.unsafeApplyEffect(input);

        assertTrue(pb.getDiningRoomCount(PawnColour.RED)==2);
        assertTrue(pb2.getDiningRoomCount(PawnColour.RED)==0);
    }

    @Test(expected = InputValidationException.class)
    public void checkEmptyInput() throws Exception {
        PlayerBoard pb2 = gb.getMutablePlayerBoardByNickname("rouge");
        CharacterCardInput input = new CharacterCardInput(pb2);
        if(card.checkInput(input)) card.unsafeApplyEffect(input);
    }
}
