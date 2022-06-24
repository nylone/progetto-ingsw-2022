package it.polimi.ingsw.Model;


import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;

import java.io.Serial;

import static it.polimi.ingsw.Constants.INPUT_NAME_TARGET_PAWN_COLOUR;

/**
 * EFFECT: Choose a color of Student: during the influence calculation this turn, that color adds no influence
 */
public class Card09 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 111L; // convention: 1 for model, (01 -> 99) for objects

    public Card09(Model ctx) {
        super(9, 3, ctx);
    }

    /**
     * Refer to: {@link CharacterCard#overridableCheckInput(CharacterCardInput)} for further information
     *
     * @param input CharacterCardInput should contain:
     *              <ul>
     *               <li>A valid PawnColour</li>>
     *              </ul>
     */
    public boolean overridableCheckInput(CharacterCardInput input) throws InputValidationException {
        if (input.getTargetPawn().isEmpty()) {
            throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_COLOUR);
        }
        return true;
    }

    /**
     * Refer to: {@link CharacterCard#unsafeApplyEffect(CharacterCardInput)} for further information
     */
    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) {
        this.context.getMutableEffects().setDeniedPawnColour(input.getTargetPawn().get());
    }

    //test purpose only
    /*@Override
    public String toString() {
        return "Card09{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }*/
}
