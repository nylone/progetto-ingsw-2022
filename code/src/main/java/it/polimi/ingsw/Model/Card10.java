package it.polimi.ingsw.Model;

public class Card10 extends StatelessEffect{
    public Card10(GameBoard ctx) {
        super(10, 1, ctx);
    }

    public int getId() {
        return this.id;
    }

    public int getCost() {
        return this.cost;
    }

    public int getTimeUsed() {
        return this.timeUsed;
    }

    public void Use(CharacterCardInput input) {

        //todo

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
