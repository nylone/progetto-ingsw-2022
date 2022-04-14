package it.polimi.ingsw.Model;


import static org.junit.Assert.*;

import it.polimi.ingsw.Exceptions.InvalidInputException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

public class Card09Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card09 card09 = new Card09(gb);
    PlayerBoard pb = new PlayerBoard(1, 1, "ari", gb.getStudentBag());

    @Test(expected = InvalidInputException.class)
    public void UseException(){
        CharacterCardInput input = new CharacterCardInput(pb);
        card09.Use(input);
    }

    @Test
    public void checkUse(){
        CharacterCardInput input = new CharacterCardInput(pb);
        PawnColour p = PawnColour.BLUE;
        input.setTargetPawn(p);

        card09.Use(input);

        assertTrue(gb.effects.getDeniedPawnColour().get().equals(p));
    }
}
