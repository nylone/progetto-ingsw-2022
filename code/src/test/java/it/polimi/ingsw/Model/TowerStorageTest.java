package it.polimi.ingsw.Model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class TowerStorageTest {
    TowerStorage ts = new TowerStorage(TowerColour.BLACK, 6);

    @Test
    public void checkThatTowerHasBeenAdded() {
        // arrange
        Tower t = new Tower(3, TowerColour.BLACK, ts);
        int initialCapacity = ts.getTowerCount();
        // act
        ts.pushTower(t);
        // assert
        assertTrue(ts.getTowerCount() == initialCapacity + 1);
    }

    @Test
    public void checkThatTowerHasBeenRemoved() {
        // arrange
        int initialCapacity = ts.getTowerCount();
        // act
        ts.extractTower();
        // assert
        assertTrue(ts.getTowerCount() == initialCapacity - 1);
    }
}
