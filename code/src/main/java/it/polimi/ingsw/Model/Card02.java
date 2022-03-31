package it.polimi.ingsw.Model;

public class Card02 extends StatelessEffect{
    public Card02(GameBoard ctx){
        super(2, 2, ctx);
    }
    public int getId(){
        return this.id;
    }

    public int getCost(){
        return this.cost;
    }

    public int getTimeUsed(){
        return this.timeUsed;
    }

    public void Use(CharacterCardInput input){
        //todo
        this.cost++;
    }

    //test purpose only
    @Override
    public String toString() {
        return "Card02{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
