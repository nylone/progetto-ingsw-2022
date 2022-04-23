package it.polimi.ingsw.Model;

import java.io.Serial;

/*
EFFECT: During the influence calculation this turn, you count as having 2 more influence
 */
public class Card08 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 110L; // convention: 1 for model, (01 -> 99) for objects

    public Card08(GameBoard ctx) {
        super(8, 2, ctx);
    }

    public void checkInput(CharacterCardInput input) {
        context.getEffects().enableIncreasedInfluence();
        addUse();
    }

    //test purpose only
    @Override
    public String toString() {
        return "Card08{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
