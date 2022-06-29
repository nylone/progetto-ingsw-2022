package it.polimi.ingsw.Model;


import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Input.DuplicateElementException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Model.Enums.TowerColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.Stack;

import static it.polimi.ingsw.Constants.INPUT_NAME_TOWER;

/**
 * A container for a set of {@link Tower}s of the same {@link TowerColour}
 */
public class TowerStorage implements Serializable {
    @Serial
    private static final long serialVersionUID = 133L; // convention: 1 for model, (01 -> 99) for objects

    private final TowerColour colour;
    private final Stack<Tower> storage;

    /**
     * Creates the storage and fills it up with towers
     * @param colour the colour of the towers in storage
     * @param amount how many towers will be added to the storage
     */
    public TowerStorage(TowerColour colour, int amount) {
        this.colour = colour;
        this.storage = new Stack<>();
        for (int i = 0; i < amount; i++) {
            this.storage.push(new Tower(colour, this));
        }
    }

    /**
     * Get the colour of the stored {@link Tower}s
     * @return the {@link TowerColour} this storage handles
     */
    public TowerColour getColour() {
        return colour;
    }

    /**
     * Get the amount of towers left in storage
     * @return the amount of {@link Tower}s left in storage
     */
    public int getTowerCount() {
        return this.storage.size();
    }

    /**
     * Extract a tower from storage.
     * @return the extracted {@link Tower} or null if the storage is empty
     */
    public Tower extractTower() {
        if (getTowerCount() == 0) return null ;
        return this.storage.pop();
    }

    /**
     * Put a {@link Tower} into storage
     * @param t the Tower to add into storage
     * @throws DuplicateElementException if the same {@link Tower} was found already present in storage
     * @throws InvalidElementException {@link Tower} if the {@link TowerColour} of the Tower is not the same as the {@link TowerColour} of the
     * storage
     */
    public void pushTower(Tower t) throws DuplicateElementException, InvalidElementException {
        boolean checkIfPresent = storage.stream()
                .anyMatch(i -> t == i);
        if (!checkIfPresent && t.getColour() == this.colour) {
            this.storage.push(t);
        } else {
            if (checkIfPresent) {
                throw new DuplicateElementException(INPUT_NAME_TOWER);
            }
            if (t.getColour() != this.colour) {
                throw new InvalidElementException(INPUT_NAME_TOWER);
            }
        }
    }
}
