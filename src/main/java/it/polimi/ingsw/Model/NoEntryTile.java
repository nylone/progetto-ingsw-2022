package it.polimi.ingsw.Model;

import java.io.Serial;
import java.io.Serializable;

/**
 * This is the representation of the game's No entry tile
 * An {@link IslandGroup} may receive a reference to an object of this type through {@link IslandGroup#addNoEntry(NoEntryTile)}.
 */
public class NoEntryTile implements Serializable {
    @Serial
    private static final long serialVersionUID = 124L; // convention: 1 for model, (01 -> 99) for objects
    private final Card05 home;

    /**
     * Generate a card and sets its "home"
     * @param card {@link Card05} is the home of every No entry tile.
     */
    public NoEntryTile(Card05 card) {
        this.home = card;
    }

    /**
     * When called, sends the tile back to its home
     */
    public void goHome() {
        home.tileReset(this);
    }
}
