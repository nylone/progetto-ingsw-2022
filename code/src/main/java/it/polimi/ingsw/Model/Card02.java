package it.polimi.ingsw.Model;

import java.io.Serial;

/*
During this turn, you take control of any
number of Professors even if you have the same
number of Students as the player who currentlly controls them.

 */
public class Card02 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 104L; // convention: 1 for model, (01 -> 99) for objects

    public Card02(GameBoard ctx) {
        super(2, 2, ctx);
    }

    public void Use(CharacterCardInput input){
        context.setAlternativeTeacherFlag(true);
        addUse();
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
