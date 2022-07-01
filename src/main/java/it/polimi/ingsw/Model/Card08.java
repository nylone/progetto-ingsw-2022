package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.OptionalValue;

import java.io.Serial;

/**
 * EFFECT: During the influence calculation this turn, you count as having 2 more influence
 */
public class Card08 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 110L; // convention: 1 for model, (01 -> 99) for objects

    public Card08(Model ctx) {
        super(8, 2, ctx);
    }

    /**
     * Refer to: {@link CharacterCard#overridableCheckInput(CharacterCardInput)} for further information
     *
     * @param input No extra parameters required
     */
    @Override
    public OptionalValue<InputValidationException> overridableCheckInput(CharacterCardInput input) {
        return OptionalValue.empty();
    }

    /**
     * Refer to: {@link CharacterCard#unsafeApplyEffect(CharacterCardInput)} for further information
     */
    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) {
        context.getMutableEffects().enableIncreasedInfluence();
    }

    //test purpose only
}
