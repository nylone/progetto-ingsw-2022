package it.polimi.ingsw.Model;

import java.util.ArrayList;

public abstract class StatefulEffect extends CharacterCard{
    protected StateType stateType;
    public StatefulEffect(int id, int cost, StateType stateType, GameBoard context){
        super(id,cost,context);
        this.stateType = stateType;
    }
    public abstract ArrayList<Object> getState();

    public abstract StateType getStateType();
}
