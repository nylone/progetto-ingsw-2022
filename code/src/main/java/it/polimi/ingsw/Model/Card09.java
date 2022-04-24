package it.polimi.ingsw.Model;


import it.polimi.ingsw.Exceptions.InputValidationException;
import it.polimi.ingsw.Exceptions.InvalidElementException;
import it.polimi.ingsw.Exceptions.toremove.InvalidInputException;

import java.io.Serial;

import static it.polimi.ingsw.Constants.INPUT_NAME_PAWN_COLOUR;
import static it.polimi.ingsw.Constants.INPUT_NAME_TARGET_ISLAND;

/*
 EFFECT: Choose a color of Student: during the influence calculation this turn, that color adds no influence
 */
public class Card09 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 111L; // convention: 1 for model, (01 -> 99) for objects

    public Card09(GameBoard ctx) {
        super(9, 3, ctx);
    }

    public boolean checkInput(CharacterCardInput input) throws InputValidationException {
        if (input.getTargetPawn().isEmpty()) {
            throw new InvalidElementException(INPUT_NAME_PAWN_COLOUR);
        }
        return true;
    }

    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        this.context.getEffects().setDeniedPawnColour(input.getTargetPawn().get());
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
