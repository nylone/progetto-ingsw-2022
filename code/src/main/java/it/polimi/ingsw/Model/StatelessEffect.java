package it.polimi.ingsw.Model;

import java.io.Serial;
import java.io.Serializable;

public abstract class StatelessEffect extends CharacterCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 128L; // convention: 1 for model, (01 -> 99) for objects

    public StatelessEffect(int id, int cost, GameBoard context) {
        super(id, cost, context);
    }
}
