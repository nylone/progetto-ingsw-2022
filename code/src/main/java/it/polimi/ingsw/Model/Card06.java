package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InputValidationException;

import java.io.Serial;

/**
 * EFFECT: When resolving a Conquering on an Island, Towers do not count towards influence.
 */
public class Card06 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 108L; // convention: 1 for model, (01 -> 99) for objects

    public Card06(GameBoard ctx) {
        super(6, 3, ctx);
    }

    public boolean overridableCheckInput(CharacterCardInput input) throws InputValidationException {
        return true; // nothing to check
    }

    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        context.getEffects().enableDenyTowerInfluence();
    }

    //test purpose only
    @Override
    public String toString() {
        return "Card06{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
