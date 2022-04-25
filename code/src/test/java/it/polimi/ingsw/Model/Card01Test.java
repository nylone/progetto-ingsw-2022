package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Operation.FailedOperationException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Card01Test {

    @Test
    public void cardShouldAlwaysHave4Students() throws Exception {
        GameBoard g = new GameBoard(GameMode.ADVANCED, "ari", "teo");
        Card01 card = new Card01(g);
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        input.setTargetPawn((PawnColour) card.getState().get(1));
        input.setTargetIsland(Utils.random(g.getMutableIslandField().getMutableGroups()).getMutableIslands().get(0));
        assertTrue(card.getState().size() == 4);
        card.unsafeApplyEffect(input);
        assertTrue(card.getState().size() == 4);
    }

    @Test
    public void usingEffectShouldAddStudentToIsland() throws Exception {
        // arrange
        GameBoard g = new GameBoard(GameMode.ADVANCED, "ari", "teo");
        Card01 card = new Card01(g);
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        Island island = Utils.random(g.getMutableIslandField().getMutableGroups()).getMutableIslands().get(0);
        int expected = island.getStudents().size();
        input.setTargetIsland(island);
        input.setTargetPawn((PawnColour) card.getState().get(1));
        // act
        card.unsafeApplyEffect(input);
        // assert
        assertTrue(island.getStudents().size() == expected + 1);
    }

    @Test(expected = FailedOperationException.class)
    public void checkUseException() throws Exception {
        GameBoard g = new GameBoard(GameMode.ADVANCED, "ari", "teo");
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        Card01 card = new Card01(g);
        assertTrue(card.getState().size()==4);
        card.unsafeApplyEffect(input);
    }
}