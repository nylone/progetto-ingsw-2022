package it.polimi.ingsw.Model;

import java.io.Serial;

/*
EFFECT: You may exchange up to 2 Students between your entrance and your Dining Room
 */
public class Card10 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 112L; // convention: 1 for model, (01 -> 99) for objects

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
        return "Card10{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
