package it.polimi.ingsw.model;
enum CharacterCardValue {
    C01,C02, C03, C04, C05, C06, C07, C08, C09, C010, C011, C012
}
public class CharacterCard {
    private CharacterCardValue value;
    private int cost;

   /* public CharacterCardValue(){
        //todo
    }*/
    public CharacterCardValue getValue() {
        return value;
    }

    public int getCost() {
        return cost;
    }

    public void increaseCost(){
        this.cost++;
    }
}
