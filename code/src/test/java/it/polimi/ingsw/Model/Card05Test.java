package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;
import static org.junit.Assert.*;

import org.junit.Test;

public class Card05Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card05 card05 = new Card05(gb);
    PlayerBoard pb = new PlayerBoard(1, 1, "ari", gb.getStudentBag());

    @Test
    public void checkUse(){
        CharacterCardInput input = new CharacterCardInput(pb);
        IslandField field = gb.getIslandField();
        input.setTargetIsland(field.getIslandById(1));

        card05.Use(input);
        assertTrue(card05.getState().size()==3); //3 tiles left after one use

        assertTrue(field.getIslandGroupById(1).getNoEntry().isPresent()); //the island group contains the NoEntryTile

        field.getIslandGroupById(1).getNoEntry().get().goHome(field.getIslandGroupById(1));

        //force return the NoEntryTile
        assertTrue(!field.getIslandGroupById(1).getNoEntry().isPresent());

        assertTrue(card05.getState().size()==4); //4 tiles left after the return of the NoEntryTile
    }
    @Test(expected = InvalidInputException.class)
    public void checkInputException(){
        CharacterCardInput input = new CharacterCardInput(pb);
        card05.Use(input);
    }
}