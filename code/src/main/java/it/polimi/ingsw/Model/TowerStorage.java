package it.polimi.ingsw.Model;


import it.polimi.ingsw.Exceptions.InvalidTowerPushException;
import it.polimi.ingsw.Model.Enums.TowerColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.Stack;

public class TowerStorage implements Serializable {
    @Serial
    private static final long serialVersionUID = 133L; // convention: 1 for model, (01 -> 99) for objects

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

    public void pushTower(Tower t) throws InvalidTowerPushException {
        boolean checkIfPresent = storage.stream()
                .anyMatch(i -> t == i);
        if (!checkIfPresent && t.getColour() == this.colour) {
            this.storage.push(t);
        }else{
            if(checkIfPresent){
                throw new InvalidTowerPushException("the tower is already in the storage");
            }
            if(t.getColour()!=this.colour){
                throw new InvalidTowerPushException("Tower's colour and Storage's Colour are different");
            }
        }
    }
}
