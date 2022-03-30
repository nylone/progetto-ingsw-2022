package it.polimi.ingsw.Model;

import java.io.Serializable;

abstract class CharacterCard implements Serializable {
    protected int id;
    protected int cost;
    protected int timeUsed;
    protected GameBoard context;

    public CharacterCard(int id, int cost, GameBoard context){
        this.id = id;
        this.cost = cost;
        this.timeUsed = 0;
        this.context = context;
    }
    public abstract int getId();
    public abstract int getCost();
    public abstract int getTimeUsed();
    public abstract void Use(CharacterCardInput input);
}
