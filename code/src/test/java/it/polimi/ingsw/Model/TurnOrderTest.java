package it.polimi.ingsw.Model;

import static org.junit.Assert.*;

import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

public class TurnOrderTest {

    @Test(expected = RuntimeException.class)
    public void checkInconsistentTurnOrder(){
        TurnOrder turnOrder = new TurnOrder();
    }
}
