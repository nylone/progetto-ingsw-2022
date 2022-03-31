package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.Stack;

public class TowerStorage implements Serializable {

    private final TowerColour colour;
    private final Stack<Tower> storage;

    public TowerStorage(TowerColour colour, int size) {
        this.colour = colour;
        this.storage = new Stack<>();
        for (int i = 0; i < size; i++) {
            this.storage.push(new Tower(i, colour, this));
        }
    }

    public TowerColour getColour() {
        return colour;
    }

    public int getTowerCount() {
        return this.storage.size();
    }

    public Tower extractTower() {
        return this.storage.pop();
    }

    public void pushTower(Tower t) {
        boolean checkIfPresent = storage.stream()
                .anyMatch(i -> t == i);
        if (!checkIfPresent && t.getColour() == this.colour) {
            this.storage.push(t);
        }
        ; // todo else throw an exception for duplicate items and invalid colour
    }
}
