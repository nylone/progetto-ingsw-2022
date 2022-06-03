package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.StateType;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class StatefulEffect extends CharacterCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 127L; // convention: 1 for model, (01 -> 99) for objects
    protected StateType stateType;

    public StatefulEffect(int id, int cost, StateType stateType, Model context) {
        super(id, cost, context);
        this.stateType = stateType;
    }

    public abstract ArrayList<Object> getState();

    public abstract StateType getStateType();
}
