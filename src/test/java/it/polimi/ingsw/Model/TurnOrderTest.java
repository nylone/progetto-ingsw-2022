package it.polimi.ingsw.Model;

import org.junit.Test;

import java.util.ArrayList;

public class TurnOrderTest {

    @Test(expected = RuntimeException.class)
    public void checkInconsistentTurnOrderWithNull() {
        new TurnOrder(null);
    }

    @Test(expected = RuntimeException.class)
    public void checkInconsistentTurnOrderWithWrongLength() {
        new TurnOrder(new ArrayList<>());
    }
}
