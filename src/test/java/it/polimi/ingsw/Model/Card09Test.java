package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test set verifies that character card 09 is able to change the way influence is calculated
 * selecting a colour that will not be counted for the influence calculation
 */
public class Card09Test {
    final Model gb = new Model(GameMode.ADVANCED, "ari", "teo"); // advanced mode needed for character cards
    final Card09 card09 = new Card09(gb);

    /**
     * Card 09 should be able to set a flag to deny a specific colour from influence calculation
     *
     */
    @Test
    public void checkUse() {
        // arrange
        // creates input to let the current player interact with card
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        PawnColour p = PawnColour.BLUE; // select the blue student to be excluded
        // input holds the selected student
        input.setTargetPawn(p);

        // act
        // activates the card to set the flag
        if (card09.checkInput(input).isEmpty()) card09.unsafeApplyEffect(input);

        // assert
        // check if the flag has been activated
        assertTrue(gb.getMutableEffects().isPawnColourDenied());
        // verifies that the denied colour is the chosen one
        assertEquals(gb.getMutableEffects().getDeniedPawnColour().get(), p);
    }

    /**
     * An invalid player action should error out.
     * <br>
     * A colour has to be selected for the action to be working
     *
     * @throws Exception an invalid input has been used to activate the card
     */
    @Test(expected = InputValidationException.class)
    public void checkInvalidInput() throws Exception {
        // creates a wrong input which will not be filled with information (colour to be excluded)
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        throw card09.checkInput(input).get();
    }
}
