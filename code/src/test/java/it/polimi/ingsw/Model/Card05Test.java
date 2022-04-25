package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Operation.FailedOperationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Card05Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card05 card05 = new Card05(gb);
    PlayerBoard pb = new PlayerBoard(1, 2, "ari", gb.getMutableStudentBag());

    @Test
    public void checkUse() throws Exception {
        CharacterCardInput input = new CharacterCardInput(pb);
        IslandField field = gb.getMutableIslandField();
        input.setTargetIsland(field.getMutableIslandById(1));
        if(card05.checkInput(input)) card05.unsafeApplyEffect(input);
        assertTrue(card05.getState().size() == 3); // 3 tiles left after one use
        assertTrue(field.getMutableIslandGroupById(1).getMutableNoEntryTiles().size() == 1); // the island group contains the NoEntryTile

        field.getMutableIslandGroupById(1).resetNoEntry();

        // force return the NoEntryTile
        assertTrue(field.getMutableIslandGroupById(1).getMutableNoEntryTiles().size() == 0);
        assertTrue(card05.getState().size() == 4); // 4 tiles left after the return of the NoEntryTile
    }

    @Test(expected = FailedOperationException.class)
    public void checkInputException() throws Exception {
        CharacterCardInput input = new CharacterCardInput(pb);
        card05.unsafeApplyEffect(input);
    }
}
