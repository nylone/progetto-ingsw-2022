package it.polimi.ingsw.Model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TowerTest {
    @Test
    public void checkTowerIsBackOnBoard() {
        // arrange
        TowerStorage ts = new TowerStorage(TowerColour.WHITE, 8);
        int initialCapacity = ts.getTowerCount();
        Tower t = new Tower(2, TowerColour.WHITE, ts);
        // act
        t.linkBackToStorage();
        // arrange
        assertTrue(ts.getTowerCount() == initialCapacity + 1);

    }
}