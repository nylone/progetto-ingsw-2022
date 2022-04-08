package it.polimi.ingsw.Model;


import it.polimi.ingsw.Exceptions.InvalidInputException;

import java.io.Serial;

/*
 EFFECT: Choose a color of Student: during the influence calculation this turn, that color adds no influence
 */
public class Card09 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 111L; // convention: 1 for model, (01 -> 99) for objects

    public Card09(GameBoard ctx) {
        super(9, 3, ctx);
    }

    public void Use(CharacterCardInput input) {
        if(input.getTargetPawn().isEmpty()){
            throw new InvalidInputException("No pawn in input");
        }else {
            this.context.setDenyPawnColourInfluence(input.getTargetPawn());
            addUse();
        }
    }

    //test purpose only
    @Override
    public String toString() {
        return "Card09{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
