package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Card02Test {

    @Test
    public void checkEffectCard02IsWorking() throws Exception {
        // arrange
        GameBoard gb = new GameBoard(GameMode.ADVANCED,"ale", "teo");
        gb.getMutablePlayerBoardById(1).addStudentToDiningRoom(PawnColour.BLUE);
        gb.getMutablePlayerBoardById(2).addStudentToDiningRoom(PawnColour.BLUE);
        gb.setTeacher(PawnColour.BLUE, gb.getMutablePlayerBoardByNickname("teo"));
        PlayerBoard greenOwner = gb.getTeachers().get(PawnColour.GREEN);
        PlayerBoard pinkOwner = gb.getTeachers().get(PawnColour.PINK);
        PlayerBoard redOwner = gb.getTeachers().get(PawnColour.RED);
        PlayerBoard yellowOwner = gb.getTeachers().get(PawnColour.YELLOW);

        Card02 card = new Card02(gb);
        CharacterCardInput activateCardAction = new CharacterCardInput(gb.getMutablePlayerBoardByNickname("ale"));
        // act
        card.unsafeApplyEffect(activateCardAction);
        // assert
        assertEquals(gb.getMutablePlayerBoardByNickname("ale"), gb.getTeachers().get(PawnColour.BLUE));
        assertEquals(greenOwner, gb.getTeachers().get(PawnColour.GREEN));
        assertEquals(pinkOwner, gb.getTeachers().get(PawnColour.PINK));
        assertEquals(redOwner, gb.getTeachers().get(PawnColour.RED));
        assertEquals(yellowOwner, gb.getTeachers().get(PawnColour.YELLOW));
    }
}
