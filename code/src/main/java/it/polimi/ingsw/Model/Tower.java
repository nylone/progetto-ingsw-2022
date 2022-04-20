package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.toremove.InvalidTowerPushException;
import it.polimi.ingsw.Model.Enums.TowerColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Tower implements Serializable {
    @Serial
    private static final long serialVersionUID = 131L; // convention: 1 for model, (01 -> 99) for objects
    private final int id;
    private final TowerColour colour;
    private final TowerStorage storage;

    public Tower(int id, TowerColour colour, TowerStorage storage) {
        if(!colour.equals(storage.getColour())){
            throw new IllegalArgumentException("Tower's colour and TowerStorage's colour are different");
        }
        this.id = id;
        this.colour = colour;
        this.storage = storage;
    }

    public TowerColour getColour() {
        return colour;
    }

    public void linkBackToStorage() {
        try {
            this.storage.pushTower(this);
        } catch (InvalidTowerPushException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tower tower = (Tower) o;
        return this.id == tower.id && this.colour == tower.colour && this.storage.equals(tower.storage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, colour, storage);
    }
}
