package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.StateType;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link CharacterCard} that implements an effect linked to some internal state
 */
public abstract class StatefulEffect extends CharacterCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 127L; // convention: 1 for model, (01 -> 99) for objects
    protected StateType stateType;

    /**
     * Construct a stateful card object
     * @param id the ID of the card
     * @param cost the cost of the card
     * @param stateType the {@link StateType} of the card
     * @param context a reference to the Model, to apply the effect.
     */
    public StatefulEffect(int id, int cost, StateType stateType, Model context) {
        super(id, cost, context);
        this.stateType = stateType;
    }

    /**
     * Get the internal state of the card. Use {@link #getStateType()} to know how to interpret it.
     * @return a {@link List} of the state objects
     */
    public abstract List<Object> getState();

    /**
     * Get the type of state this card uses.
     * @return a variant of {@link StateType}
     */
    public abstract StateType getStateType();
}
