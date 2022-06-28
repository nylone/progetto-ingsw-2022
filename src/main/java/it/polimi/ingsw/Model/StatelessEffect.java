package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.StateType;

import java.io.Serial;
import java.io.Serializable;

/**
 * A {@link CharacterCard} that implements an effect NOT linked to some internal state
 */
public abstract class StatelessEffect extends CharacterCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 128L; // convention: 1 for model, (01 -> 99) for objects

    /**
     * Construct a stateless card object
     * @param id the ID of the card
     * @param cost the cost of the card
     * @param context a reference to the Model, to apply the effect.
     */
    public StatelessEffect(int id, int cost, Model context) {
        super(id, cost, context);
    }
}
