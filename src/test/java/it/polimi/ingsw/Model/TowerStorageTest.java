package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.DuplicateElementException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Model.Enums.TowerColour;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class TowerStorageTest {
    TowerStorage ts = new TowerStorage(TowerColour.BLACK, 6);

    @Test
    public void checkThatTowerHasBeenAdded() throws InvalidElementException, DuplicateElementException {
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

    @Test(expected = IllegalArgumentException.class)
    public void checkTowerCreationException() {
        Tower t = new Tower(3, TowerColour.BLACK, ts);
        // Tower t2 = new Tower(3, TowerColour.BLACK, ts);
        Tower t3 = new Tower(5, TowerColour.WHITE, ts);
    }

    @Test(expected = InvalidElementException.class)
    public void checkIllegalColourPush() throws InvalidElementException, DuplicateElementException {
        Tower t = new Tower(3, TowerColour.BLACK, ts);
        TowerStorage ts2 = new TowerStorage(TowerColour.WHITE, 6);
        ts2.extractTower();
        ts2.pushTower(t);

    }

    @Test(expected = DuplicateElementException.class)
    public void checkIllegalDuplicatePush() throws DuplicateElementException, InvalidElementException {
        ts.extractTower();
        ts.extractTower();
        Tower t = new Tower(3, TowerColour.BLACK, ts);

        ts.pushTower(t);
        ts.pushTower(t);
    }
}
