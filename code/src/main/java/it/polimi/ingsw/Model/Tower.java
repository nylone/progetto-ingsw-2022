package it.polimi.ingsw.Model;

import java.util.Objects;

public class Tower {
    private final int id;
    private final TowerColour colour;
    private final TowerStorage storage;

    public Tower(int id, TowerColour colour, TowerStorage storage) {
        this.id = id;
        this.colour = colour;
        this.storage = storage;
    }

    public TowerColour getColour() {
        return colour;
    }

    public void linkBackToStorage() {
        this.storage.pushTower(this);
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
