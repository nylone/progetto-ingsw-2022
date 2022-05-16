package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.TowerColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
        assertEquals(ts.getTowerCount(), initialCapacity + 1);

    }
}