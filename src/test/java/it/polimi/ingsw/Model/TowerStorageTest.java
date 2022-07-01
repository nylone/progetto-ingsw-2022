package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.DuplicateElementException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Model.Enums.TowerColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TowerStorageTest {
    final TowerStorage ts = new TowerStorage(TowerColour.BLACK, 6);

    @Test
    public void checkThatTowerHasBeenAdded() throws InvalidElementException, DuplicateElementException {
        // arrange
        Tower t = new Tower(TowerColour.BLACK, ts);
        int initialCapacity = ts.getTowerCount();
        // act
        ts.pushTower(t);
        // assert
        assertEquals(ts.getTowerCount(), initialCapacity + 1);
    }

    @Test
    public void checkThatTowerHasBeenRemoved() {
        // arrange
        int initialCapacity = ts.getTowerCount();
        // act
        ts.extractTower();
        // assert
        assertEquals(ts.getTowerCount(), initialCapacity - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void linkingTowerWithWrongStorage() {
        // trying to link a white tower to a black tower storage
        new Tower(TowerColour.WHITE, ts);
    }

    @Test(expected = InvalidElementException.class)
    public void checkIllegalColourPush() throws InvalidElementException, DuplicateElementException {
        TowerStorage whiteTowerStorage = new TowerStorage(TowerColour.WHITE, 6);
        Tower blackTower = new Tower(TowerColour.BLACK, ts); // ts is a black tower storage
        // trying to push a black tower into a white tower storage
        whiteTowerStorage.pushTower(blackTower);
    }

    @Test(expected = DuplicateElementException.class)
    public void checkIllegalDuplicatePush() throws DuplicateElementException, InvalidElementException {
        Tower t = new Tower(TowerColour.BLACK, ts);

        // trying to push same tower twice
        ts.pushTower(t);
        ts.pushTower(t);
    }
}
