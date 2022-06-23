package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Card02And04Test {

    /**
     * Character card 02 should leave the teachers control untouched except for the teachers of whom multiple players
     * hold same influence
     *
     * @throws Exception is thrown when an invalid card activation happens, but this test should not error out
     *                   (by definition)
     */
    @Test
    public void checkEffectCard02IsWorking() throws Exception {
        // arrange
        Model gb = new Model(GameMode.ADVANCED, "ale", "teo"); // advanced mode needed for character cards
        // giving the same influence over a common teacher (BLUE) to both players
        gb.getMutablePlayerBoardById(0).unsafeAddStudentToDiningRoom(PawnColour.BLUE);
        gb.getMutablePlayerBoardById(1).unsafeAddStudentToDiningRoom(PawnColour.BLUE);
        // assigning blue teacher to one player
        gb.setTeacher(PawnColour.BLUE, gb.getMutablePlayerBoardByNickname("teo"));
        // saving information about initial teachers' control situation
        PlayerBoard greenOwner = gb.getTeachers().get(PawnColour.GREEN);
        PlayerBoard pinkOwner = gb.getTeachers().get(PawnColour.PINK);
        PlayerBoard redOwner = gb.getTeachers().get(PawnColour.RED);
        PlayerBoard yellowOwner = gb.getTeachers().get(PawnColour.YELLOW);

        Card02 card = new Card02(gb);
        // creates input to let the player interact with card
        CharacterCardInput activateCardAction = new CharacterCardInput(gb.getMutablePlayerBoardByNickname("ale"));

        // act
        // activates the card to validate teachers control
        if(card.overridableCheckInput(new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer())))
            card.unsafeApplyEffect(activateCardAction);

        // assert
        // blue teacher should have been assigned to the player who used the card 02
        assertEquals(gb.getMutablePlayerBoardByNickname("ale"), gb.getTeachers().get(PawnColour.BLUE));
        // the other teachers' control should remain unchanged
        assertEquals(greenOwner, gb.getTeachers().get(PawnColour.GREEN));
        assertEquals(pinkOwner, gb.getTeachers().get(PawnColour.PINK));
        assertEquals(redOwner, gb.getTeachers().get(PawnColour.RED));
        assertEquals(yellowOwner, gb.getTeachers().get(PawnColour.YELLOW));
    }

    @Test
    public void checkEffectCard04IsWorking() throws Exception{
        Model gb = new Model(GameMode.ADVANCED, "ale", "teo"); // advanced mode needed for character cards
        Card04 card04 = new Card04(gb);
        CharacterCardInput activateCardAction = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());

        if(card04.overridableCheckInput(activateCardAction))
            card04.unsafeUseCard(activateCardAction);

        assertTrue(gb.getMutableEffects().isMotherNatureMovementIncreased());
    }
}
