package it.polimi.ingsw.Model.Enums;

import it.polimi.ingsw.Model.StatefulEffect;

import java.io.Serial;
import java.io.Serializable;

/**
 * {@link it.polimi.ingsw.Model.StatefulEffect} cards contain an internal state, this Enumeration allows for simple identification
 * of the internal state through {@link StatefulEffect#getStateType()}
 */
public enum StateType implements Serializable {
    PAWNCOLOUR,
    NOENTRY;
    @Serial
    private static final long serialVersionUID = 129L; // convention: 1 for model, (01 -> 99) for objects
}
