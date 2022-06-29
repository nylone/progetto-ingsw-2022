package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Model.Enums.TowerColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TowerTest {
    @Test
    public void checkTowerIsBackOnBoard() {
        // arrange
        TowerStorage ts = new TowerStorage(TowerColour.WHITE, 8);
        int initialCapacity = ts.getTowerCount();
        Tower t = new Tower(TowerColour.WHITE, ts);
        // act
        try {
            t.linkBackToStorage();
        } catch (InvalidElementException e) {
            throw new RuntimeException(e);
        }
        // arrange
        assertEquals(ts.getTowerCount(), initialCapacity + 1);

    }
}