package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.StateType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * This test set verifies that character card 05 is able to manage, receive and give no entry tiles.
 * <br>
 * It should also be able to understand to which destination the tile is destined or error out
 */
public class Card05Test {
    final Model gb = new Model(GameMode.ADVANCED, "ari", "teo"); // advanced mode needed for character cards
    final Card05 card05 = new Card05(gb);

    /**
     * Card 05 should ba able to hold 4 entry tile and when activated to put one tile on the chosen island.
     * If the island doesn't need it anymore the card should be able to receive the tile used
     *
     * @throws Exception is thrown when an invalid card activation happens, but this test should not error out
     *                   (by definition)
     */
    @Test
    public void checkUse() throws Exception {
        // arrange
        // card should only hold no entry tile items
        assertSame(card05.getStateType(), StateType.NOENTRY);
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();


        // creates input to let the current player interact with card
        CharacterCardInput input = new CharacterCardInput(pb);
        IslandField field = gb.getMutableIslandField();
        // input holds the selected island
        input.setTargetIsland(field.getMutableIslandById(1));

        // act
        // activates the card to resolve the influence on the island
        if (card05.checkInput(input).isEmpty()) card05.unsafeApplyEffect(input);
        assertEquals(3, card05.getState().size()); // 3 tiles left after one use
        // the island group contains the NoEntryTile
        assertEquals(1, field.getMutableIslandGroupById(1).getMutableNoEntryTiles().size());

        // simulate mother nature effect on island and returning tile to card 05
        field.getMutableIslandGroupById(1).resetNoEntry();

        // force return the NoEntryTile
        assertEquals(0, field.getMutableIslandGroupById(1).getMutableNoEntryTiles().size());
        assertEquals(4, card05.getState().size()); // 4 tiles left after the return of the NoEntryTile
    }

    /**
     * An invalid player action should error out.
     * <br>
     * An island has to be selected for the action to be working
     *
     * @throws Exception an invalid input has been used to activate the card
     */
    @Test(expected = InputValidationException.class)
    public void checkInputException() throws Exception {
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        throw card05.checkInput(input).get();
    }

    /**
     * An invalid player action should error out.
     * <br>
     * An island must have a valid id to be selected
     *
     * @throws Exception an invalid input has been used to activate the card
     */
    @Test(expected = InputValidationException.class)
    public void checkInvalidIslandInput() throws Exception {
        // creates a wrong input (player will select an invalid island)
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        Island island = new Island(13);
        input.setTargetIsland(island);
        throw card05.checkInput(input).get();
    }

    /**
     * An invalid player action should error out.
     * <br>
     * An island must exist in the island field to be selected
     *
     * @throws Exception an invalid input has been used to activate the card
     */
    @Test(expected = InputValidationException.class)
    public void checkIslandNotInField() throws Exception {
        // creates a wrong input (player will select an invalid island)
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        Island island = new Island(8);
        input.setTargetIsland(island);
        throw card05.checkInput(input).get();
    }

    /**
     * If the card has already given out all of its tiles, it should error out when prompted for handling out more
     */
    @Test(expected = InputValidationException.class)
    public void checkEmptyCard() throws Exception {
        // arrange
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        // creates input to let the current player interact with card
        CharacterCardInput input = new CharacterCardInput(pb);
        IslandField field = gb.getMutableIslandField();
        // input holds the selected island
        input.setTargetIsland(field.getMutableIslandById(1));
        // emptying card of all of its entry tiles
        for (int i = 0; i < 4; i++) {
            if (card05.checkInput(input).isEmpty()) card05.unsafeApplyEffect(input);
        }

        // act
        // trying to use card when it is empty
        throw card05.checkInput(input).get();
    }
}
