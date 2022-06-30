package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test set verifies that the card 03 is able to change the control of an island leaving mother nature still
 */
public class Card03Test {
    Model gb = new Model(GameMode.ADVANCED, "ari", "teo"); // advanced mode needed for character cards
    Card03 card = new Card03(gb);

    /**
     * Card 03 should be able to change influence on an island according to rules without moving mother nature on it
     *
     * @throws Exception is thrown when an invalid card activation happens, but this test should not error out
     *                   (by definition)
     */
    @Test
    public void checkUse() throws Exception {
        // arrange
        PlayerBoard pb1 = gb.getMutablePlayerBoard(0);
        PlayerBoard pb2 = gb.getMutablePlayerBoard(1);
        IslandGroup ig = gb.getMutableIslandField().getMutableIslandGroupById(5);
        // select an island on which students will be placed
        Island island = ig.getMutableIslands().get(0);
        // find mother nature position (should remain unchanged)
        IslandGroup expectedMotherNaturePosition = gb.getMutableIslandField().getMutableMotherNaturePosition();

        // adding students to island
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.YELLOW);

        // assigning teachers to players to let first player win
        gb.setTeacher(PawnColour.BLUE, pb1);
        gb.setTeacher(PawnColour.YELLOW, pb2);

        // creates input to let the player interact with card
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        // input holds the selected island
        input.setTargetIsland(island);

        // act
        // activates the card to resolve the influence on the island
        if (card.checkInput(input).isEmpty()) card.unsafeApplyEffect(input);

        // assert
        // tower colour should reflect influence given to first player
        assertEquals(ig.getTowerColour().get(), gb.getTeamMapper().getMutableTowerStorage(pb1).getColour());
        // mother nature should still be on the initial island
        assertEquals(expectedMotherNaturePosition, gb.getMutableIslandField().getMutableMotherNaturePosition());
    }

    /**
     * An invalid player action should error out.
     * <br>
     * An island has to be selected for the action to be working
     *
     * @throws Exception an invalid input has been used to activate the card
     */
    @Test(expected = InputValidationException.class)
    public void checkInvalidInput() throws Exception {
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        throw card.checkInput(input).get();
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
        throw card.checkInput(input).get();
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
        throw card.checkInput(input).get();
    }
}
