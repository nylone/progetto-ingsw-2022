package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.DuplicateElementException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Model.Enums.TowerColour;

import java.io.Serial;
import java.io.Serializable;

/**
 * Allows for the representation of the game's tower pawn
 */
public class Tower implements Serializable {
    @Serial
    private static final long serialVersionUID = 131L; // convention: 1 for model, (01 -> 99) for objects
    private final TowerColour colour;
    private final TowerStorage storage;

    /**
     * Creates a Tower by assigning it a colour and a storage. A tower may find itself inside the storage or outside of it,
     * in which case it has the ability to return to its storage on its own.
     * @param colour the {@link TowerColour} assigned to the tower
     * @param storage the {@link TowerStorage} object responsible for storing towers during the game
     */
    public Tower(TowerColour colour, TowerStorage storage) {
        if (!colour.equals(storage.getColour())) {
            throw new IllegalArgumentException("Tower's colour and TowerStorage's colour are different");
        }
        this.colour = colour;
        this.storage = storage;
    }

    /**
     * Get the colour of the tower
     * @return the {@link TowerColour} of the tower
     */
    public TowerColour getColour() {
        return colour;
    }

    /**
     * Send the tower back to its {@link TowerStorage}. If the tower is already back in storage, then nothing is done.
     * @throws InvalidElementException if {@link #getColour()} is different than {@link TowerStorage#getColour()}
     */
    public void linkBackToStorage() throws InvalidElementException {
        try {
            this.storage.pushTower(this);
        } catch (DuplicateElementException e) {
            Logger.warning("a tower that was already in storage tried to get back into it");
        }
    }
}
