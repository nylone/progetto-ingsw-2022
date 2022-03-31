package it.polimi.ingsw.Model;
/*
You may move Mother Nature up to 2
additional Islands than is indicated by the Assistant
card you've played.
 */
public class Card04 extends StatelessEffect {
    public Card04(GameBoard ctx) {
        super(4, 1, ctx);
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
