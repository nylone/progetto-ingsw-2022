package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.DuplicateElementException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Model.Enums.TowerColour;

import java.io.Serial;
import java.io.Serializable;

public class Tower implements Serializable {
    @Serial
    private static final long serialVersionUID = 131L; // convention: 1 for model, (01 -> 99) for objects
    private final int id;
    private final TowerColour colour;
    private final TowerStorage storage;

    public Tower(int id, TowerColour colour, TowerStorage storage) {
        if (!colour.equals(storage.getColour())) {
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
        } catch (DuplicateElementException | InvalidElementException e) {
            e.printStackTrace();
        }
    }
}
