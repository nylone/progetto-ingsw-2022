package it.polimi.ingsw.Model;

import java.io.Serial;
import java.io.Serializable;

public abstract class CharacterCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 115L; // convention: 1 for model, (01 -> 99) for objects

    protected int id;
    protected int cost;
    protected int timeUsed;
    protected GameBoard context;

    public CharacterCard(int id, int cost, GameBoard context) {
        this.id = id;
        this.cost = cost;
        this.timeUsed = 0;
        this.context = context;
    }


    public int getId() {
        return this.id;
    }
    public int getCost() {
        return this.timeUsed > 0 ? this.cost + 1 : this.cost;
    }
    public int getTimeUsed() {
        return this.timeUsed;
    }
    public void addUse() {this.timeUsed++;}

    public abstract void Use(CharacterCardInput input);

}
