package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.*;
public class Card03Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card03 card = new Card03(gb);

    @Test
    public void checkUse() {
        PlayerBoard pb1 = gb.getPlayerBoardByNickname("ari");
        PlayerBoard pb2 = gb.getPlayerBoardByNickname("teo");
        IslandGroup ig = gb.getIslandField().getIslandGroupById(5);
        Island island = ig.getIslands().get(0);
        IslandGroup expectedMotherNaturePosition = gb.getIslandField().getMotherNaturePosition();

        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.YELLOW);

        gb.setTeacher(PawnColour.BLUE, pb1);
        gb.setTeacher(PawnColour.YELLOW, pb2);

        CharacterCardInput input = new CharacterCardInput(pb1);
        input.setTargetIsland(island);
        card.Use(input);

        assertTrue(ig.getTowerColour().get().equals(gb.getTeamMap().getTowerStorage(pb1).getColour()));
        assertEquals(expectedMotherNaturePosition, gb.getIslandField().getMotherNaturePosition());
    }

    @Test(expected = InvalidInputException.class)
    public void checkInvalidInput(){
        PlayerBoard pb1 = gb.getPlayerBoardByNickname("ari");
        CharacterCardInput input = new CharacterCardInput(pb1);
        card.Use(input);
    }
}
