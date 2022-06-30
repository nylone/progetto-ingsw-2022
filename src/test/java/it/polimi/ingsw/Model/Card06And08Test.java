package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Card06And08Test {
    Model gb = new Model(GameMode.ADVANCED, "ari", "teo"); // advanced mode needed for character cards
    Card06 card06 = new Card06(gb);
    Card08 card08 = new Card08(gb);

    /**
     * Card 06 should be able to set a flag in the model context
     *
     * @throws Exception @throws Exception is thrown when an invalid card activation happens, but this test should not error out
     *                   (by definition)
     */
    @Test
    public void checkUse06() throws Exception {
        // arrange
        // creates input to let the current player interact with card
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());

        // act
        // activates the card to resolve the influence on the island
        if (card06.checkInput(input).isEmpty()) card06.unsafeApplyEffect(input);

        // assert
        // flag should be true
        assertTrue(gb.getMutableEffects().isTowerInfluenceDenied());
    }

    /**
     * Card 08 should be able to give 2 more influence points when activated
     *
     * @throws Exception @throws Exception is thrown when an invalid card activation happens, but this test should not error out
     *                   (by definition)
     */
    @Test
    public void checkUse08() throws Exception {
        // arrange
        Island island = gb.getMutableIslandField().getMutableIslandGroupById(1).getMutableIslands().get(0);
        IslandGroup islandGroup = gb.getMutableIslandField().getMutableIslandGroupById(0);
        PlayerBoard pb2 = gb.getMutablePlayerBoard(1);
        TurnOrder turnOrder = gb.getMutableTurnOrder();
        island.addStudent(PawnColour.RED);
        gb.setTeacher(PawnColour.RED, pb2);

        turnOrder.setSelectedCard(turnOrder.getMutableCurrentPlayer(),
                turnOrder.getMutableCurrentPlayer().getMutableAssistantCards().get(1));
        turnOrder.stepToNextPlayer();
        turnOrder.setSelectedCard(turnOrder.getMutableCurrentPlayer(),
                turnOrder.getMutableCurrentPlayer().getMutableAssistantCards().get(0));
        turnOrder.stepToNextPlayer();

        CharacterCardInput input = new CharacterCardInput(turnOrder.getMutableCurrentPlayer());
        if (card08.checkInput(input).isEmpty()) card08.unsafeApplyEffect(input);

        assertEquals(gb.getInfluencerOf(islandGroup).get(), gb.getTeamMapper().getTeamID(turnOrder.getMutableCurrentPlayer()));
        /*in this test, adding 2 points due to card8's effect will always give the influence to the caller; in fact:
                teo caller --> 3 points of influence teo / 0 points of influence ari
                ari caller --> 2 points of influence ari / 1 points of influence teo
         */
    }
}
