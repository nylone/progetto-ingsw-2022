package it.polimi.ingsw.Model;

import java.io.Serializable;

abstract class CharacterCard implements Serializable {
    private int id;
    private int cost;
    private int timeUsed;
    private GameBoard context;

    public CharacterCard(int id, int cost, GameBoard context){
        this.id = id;
        this.cost = cost;
        this.timeUsed = 0;
        this.context = context;
    }
    public abstract int getId();
    public abstract int getCost();
    public abstract int getTimeUsed();
    public abstract void use(CharacterCardInput input);
}
