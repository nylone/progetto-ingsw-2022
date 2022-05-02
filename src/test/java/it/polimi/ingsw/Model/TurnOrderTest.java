package it.polimi.ingsw.Model;

import org.junit.Test;

public class TurnOrderTest {

    @Test(expected = RuntimeException.class)
    public void checkInconsistentTurnOrder() {
        TurnOrder turnOrder = new TurnOrder();
    }
}
