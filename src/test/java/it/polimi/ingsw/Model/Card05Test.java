package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.StateType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class Card05Test {
    Model gb = new Model(GameMode.ADVANCED, "ari", "teo");
    Card05 card05 = new Card05(gb);

    @Test
    public void checkUse() throws Exception {
        assertSame(card05.getStateType(), StateType.NOENTRY);
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();

        CharacterCardInput input = new CharacterCardInput(pb);
        IslandField field = gb.getMutableIslandField();
        input.setTargetIsland(field.getMutableIslandById(1));
        if (card05.checkInput(input)) card05.unsafeApplyEffect(input);
        assertEquals(3, card05.getState().size()); // 3 tiles left after one use
        assertEquals(1, field.getMutableIslandGroupById(1).getMutableNoEntryTiles().size()); // the island group contains the NoEntryTile

        field.getMutableIslandGroupById(1).resetNoEntry();

        // force return the NoEntryTile
        assertEquals(0, field.getMutableIslandGroupById(1).getMutableNoEntryTiles().size());
        assertEquals(4, card05.getState().size()); // 4 tiles left after the return of the NoEntryTile
    }

    @Test(expected = InputValidationException.class)
    public void checkInputException() throws Exception {
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        if (card05.checkInput(input)) card05.unsafeApplyEffect(input);
    }

    @Test(expected = InputValidationException.class)
    public void checkInvalidIslandInput() throws Exception {
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        Island island = new Island(13);
        input.setTargetIsland(island);
        if (card05.checkInput(input)) card05.unsafeApplyEffect(input);
    }

    @Test(expected = InputValidationException.class)
    public void checkIslandNotInField() throws Exception {
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        Island island = new Island(8);
        input.setTargetIsland(island);
        if (card05.checkInput(input)) card05.unsafeApplyEffect(input);
    }

    @Test(expected = InputValidationException.class)
    public void checkEmptyCard() throws Exception {
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        CharacterCardInput input = new CharacterCardInput(pb);
        IslandField field = gb.getMutableIslandField();
        input.setTargetIsland(field.getMutableIslandById(1));
        if (card05.checkInput(input)) card05.unsafeApplyEffect(input);
        input.setTargetIsland(field.getMutableIslandById(1));
        if (card05.checkInput(input)) card05.unsafeApplyEffect(input);
        input.setTargetIsland(field.getMutableIslandById(1));
        if (card05.checkInput(input)) card05.unsafeApplyEffect(input);
        input.setTargetIsland(field.getMutableIslandById(1));
        if (card05.checkInput(input)) card05.unsafeApplyEffect(input);

        input.setTargetIsland(field.getMutableIslandById(1));
        if (card05.checkInput(input)) card05.unsafeApplyEffect(input);
    }
}
