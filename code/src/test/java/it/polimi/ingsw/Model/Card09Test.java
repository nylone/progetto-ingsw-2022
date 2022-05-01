package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Card09Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card09 card09 = new Card09(gb);

    @Test
    public void checkUse() throws Exception {
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        PawnColour p = PawnColour.BLUE;
        input.setTargetPawn(p);

        if (card09.checkInput(input)) card09.unsafeApplyEffect(input);

        assertTrue(gb.getMutableEffects().isPawnColourDenied());
        assertTrue(gb.getMutableEffects().getDeniedPawnColour().get().equals(p));
    }

    @Test(expected = InputValidationException.class)
    public void checkInvalidInput() throws Exception {
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        if (card09.checkInput(input)) card09.unsafeApplyEffect(input);
    }
}
