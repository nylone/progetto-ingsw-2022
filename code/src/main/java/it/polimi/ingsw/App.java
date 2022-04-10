package it.polimi.ingsw;

import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.IslandGroup;
import it.polimi.ingsw.Model.Enums.TowerColour;
import it.polimi.ingsw.Model.TowerStorage;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        IslandGroup ig1 = new IslandGroup(new Island(1));
        IslandGroup ig2 = new IslandGroup(new Island(2));
        TowerStorage ts1 = new TowerStorage(TowerColour.BLACK, 7);
        TowerStorage ts2 = new TowerStorage(TowerColour.WHITE, 7);

        System.out.println(ig1.isJoinable(ig2));

        ig1.swapTower(ts1);
        ig2.swapTower(ts2);
        System.out.println(ig1.isJoinable(ig2));

        ig2.swapTower(ts1);
        System.out.println(ig1.isJoinable(ig2));
    }
}
