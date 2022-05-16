package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;
import org.junit.Test;

import static org.junit.Assert.*;

public class Card01Test {

    @Test
    public void cardShouldAlwaysHave4Students() throws Exception {
        GameBoard g = new GameBoard(GameMode.ADVANCED, "ari", "teo");
        Card01 card = new Card01(g);
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        input.setTargetPawn((PawnColour) card.getState().get(1));
        input.setTargetIsland(Utils.random(g.getMutableIslandField().getMutableGroups()).getMutableIslands().get(0));
        assertEquals(4, card.getState().size());
        if (card.checkInput(input)) card.unsafeApplyEffect(input);
        assertEquals(4, card.getState().size());
        assertSame(card.getStateType(), StateType.PAWNCOLOUR);
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
        if (card.checkInput(input)) card.unsafeApplyEffect(input);
        // assert
        assertEquals(island.getStudents().size(), expected + 1);
        assertTrue(island.getStudents().contains(input.getTargetPawn().get()));
    }

    @Test(expected = InputValidationException.class)
    public void checkUseException() throws Exception {
        GameBoard g = new GameBoard(GameMode.ADVANCED, "ari", "teo");
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        Card01 card = new Card01(g);
        assertEquals(4, card.getState().size());
        if (card.checkInput(input)) card.unsafeApplyEffect(input);
    }
}