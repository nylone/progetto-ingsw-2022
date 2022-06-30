package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.OptionalValue;

import java.io.Serial;

/**
 * EFFECT: When resolving a Conquering on an Island, Towers do not count towards influence.
 */
public class Card06 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 108L; // convention: 1 for model, (01 -> 99) for objects

    public Card06(Model ctx) {
        super(6, 3, ctx);
    }

    /**
     * Refer to: {@link CharacterCard#overridableCheckInput(CharacterCardInput)} for further information
     *
     * @param input No extra parameters required
     */
    public OptionalValue<InputValidationException> overridableCheckInput(CharacterCardInput input) {
        return OptionalValue.empty(); // nothing to check
    }

    /**
     * Refer to: {@link CharacterCard#unsafeApplyEffect(CharacterCardInput)} for further information
     */
    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        context.getMutableEffects().enableDenyTowerInfluence();
    }

    /*//test purpose only
    @Override
    public String toString() {
        return "Card06{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
    //*/
}
