package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Operation.FailedOperationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class Card03Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card03 card = new Card03(gb);

    @Test
    public void checkUse() throws Exception {
        PlayerBoard pb1 = gb.getMutablePlayerBoardByNickname("ari");
        PlayerBoard pb2 = gb.getMutablePlayerBoardByNickname("teo");
        IslandGroup ig = gb.getMutableIslandField().getMutableIslandGroupById(5);
        Island island = ig.getMutableIslands().get(0);
        IslandGroup expectedMotherNaturePosition = gb.getMutableIslandField().getMutableMotherNaturePosition();

        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.YELLOW);

        gb.setTeacher(PawnColour.BLUE, pb1);
        gb.setTeacher(PawnColour.YELLOW, pb2);

        CharacterCardInput input = new CharacterCardInput(pb1);
        input.setTargetIsland(island);
        card.unsafeApplyEffect(input);

        assertTrue(ig.getTowerColour().get().equals(gb.getTeamMap().getMutableTowerStorage(pb1).getColour()));
        assertEquals(expectedMotherNaturePosition, gb.getMutableIslandField().getMutableMotherNaturePosition());
    }

    @Test(expected = FailedOperationException.class)
    public void checkInvalidInput() throws Exception {
        PlayerBoard pb1 = gb.getMutablePlayerBoardByNickname("ari");
        CharacterCardInput input = new CharacterCardInput(pb1);
        card.unsafeApplyEffect(input);
    }
}
