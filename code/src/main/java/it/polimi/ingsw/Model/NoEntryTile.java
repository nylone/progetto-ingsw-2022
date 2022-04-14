package it.polimi.ingsw.Model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

public class NoEntryTile implements Serializable {
    @Serial
    private static final long serialVersionUID = 124L; // convention: 1 for model, (01 -> 99) for objects
    private final Card05 home;

    public NoEntryTile(Card05 card) {
        this.home = card;
    }

    public void goHome() {
        home.tileReset(this);
    }

}
