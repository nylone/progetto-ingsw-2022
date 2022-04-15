package it.polimi.ingsw.Model;

import static org.junit.Assert.*;

import it.polimi.ingsw.Model.Enums.GameMode;
import org.junit.Test;

public class Card06And08Test{
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card06 card06 = new Card06(gb);
    Card08 card08 = new Card08(gb);
    PlayerBoard pb = new PlayerBoard(1, 1, "ari", gb.getStudentBag());
    @Test
    public void checkUse06(){
        CharacterCardInput input = new CharacterCardInput(pb);
        card06.Use(input);

        assertTrue(gb.effects.getDenyTowerInfluence());
    }
    @Test
    public void checkUse08(){
        CharacterCardInput input = new CharacterCardInput(pb);
        card08.Use(input);

        assertTrue(gb.effects.getIncreasedInfluenceIsActive());
    }
}
