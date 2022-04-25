package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Card09Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card09 card09 = new Card09(gb);
    PlayerBoard pb = new PlayerBoard(1, 2, "ari", gb.getMutableStudentBag());

    @Test
    public void checkUse(){
        CharacterCardInput input = new CharacterCardInput(pb);
        PawnColour p = PawnColour.BLUE;
        input.setTargetPawn(p);

        card09.unsafeApplyEffect(input);

        assertTrue(gb.getMutableEffects().getDeniedPawnColour().get().equals(p));
    }
}
