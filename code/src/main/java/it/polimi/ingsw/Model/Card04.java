package it.polimi.ingsw.Model;

import java.io.Serial;

/*
You may move Mother Nature up to 2
additional Islands than is indicated by the Assistant
card you've played.
 */
public class Card04 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 106L; // convention: 1 for model, (01 -> 99) for objects

    public Card04(GameBoard ctx) {
        super(4, 1, ctx);
    }

    public void Use(CharacterCardInput input) {
        //todo
        addUse();
    }

    //test purpose only
    @Override
    public String toString() {
        return "Card04{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
