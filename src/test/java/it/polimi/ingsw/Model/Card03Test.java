package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Card03Test {
    Model gb = new Model(GameMode.ADVANCED, "ari", "teo");
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

        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        input.setTargetIsland(island);
        if (card.checkInput(input)) card.unsafeApplyEffect(input);

        assertEquals(ig.getTowerColour().get(), gb.getTeamMapper().getMutableTowerStorage(pb1).getColour());
        assertEquals(expectedMotherNaturePosition, gb.getMutableIslandField().getMutableMotherNaturePosition());
    }

    @Test(expected = InputValidationException.class)
    public void checkInvalidInput() throws Exception {
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        if (card.checkInput(input)) card.unsafeApplyEffect(input);
    }

    @Test(expected = InputValidationException.class)
    public void checkInvalidIslandInput() throws Exception {
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        Island island = new Island(13);
        input.setTargetIsland(island);
        if (card.checkInput(input)) card.unsafeApplyEffect(input);
    }

    @Test(expected = InputValidationException.class)
    public void checkIslandNotInField() throws Exception {
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        Island island = new Island(8);
        input.setTargetIsland(island);
        if (card.checkInput(input)) card.unsafeApplyEffect(input);
    }
}
