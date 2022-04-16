package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;
public class Card03Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card03 card = new Card03(gb);

    @Test
    public void checkUse(){
        Optional<IslandGroup> tg = Optional.empty();
        PlayerBoard pb1 = gb.getPlayerBoardByNickname("ari");
        PlayerBoard pb2 = gb.getPlayerBoardByNickname("teo");
        Island island = gb.getIslandField().getIslandById(5);
        for(IslandGroup ig : gb.getIslandField().getGroups()){
             if(ig.contains(island)){ break;}
        }
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.YELLOW);

        gb.setTeacher(PawnColour.BLUE, pb1);
        gb.setTeacher(PawnColour.YELLOW, pb2);

        CharacterCardInput input = new CharacterCardInput(pb1);
        input.setTargetIsland(island);
        card.Use(input);

        assertTrue(tg.get().getTowerColour().get().equals(gb.getTeamMap().getTowerStorage(pb1).getColour()));

    }

    @Test(expected = InvalidInputException.class)
    public void checkInvalidInput(){
        PlayerBoard pb1 = gb.getPlayerBoardByNickname("ari");
        CharacterCardInput input = new CharacterCardInput(pb1);
        card.Use(input);
    }

}
