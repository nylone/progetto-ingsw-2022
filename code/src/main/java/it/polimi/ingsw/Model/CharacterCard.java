package it.polimi.ingsw.Model;

import java.io.Serializable;

abstract class CharacterCard implements Serializable {
    private int id;
    private Character character;
    private int cost;
    private int timeUsed;

    public CharacterCard(Character character, int cost){
        this.character = character;
        this.cost = cost;
    }
    public abstract int getId();
    public abstract Character getCharacter();
    public abstract int getCost();
    public abstract int getTimeUsed();
}
