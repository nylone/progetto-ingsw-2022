package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Exceptions.Operation.FailedOperationException;
import it.polimi.ingsw.Misc.OptionalValue;

import java.io.Serial;

/**
 * EFFECT: Choose an Island and resolve the Island as if
 * Mother Nature had ended her movement there. Mother
 * Nature will still move and the Island where she ends
 * her movement will also be resolved.
 */
public class Card03 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 105L; // convention: 1 for model, (01 -> 99) for objects

    public Card03(Model ctx) {
        super(3, 3, ctx);
    }

    /**
     * Refer to: {@link CharacterCard#overridableCheckInput(CharacterCardInput)} for further information
     *
     * @param input CharacterCardInput should contain:
     *              <ul>
     *               <li>A valid island's ID </li>
     *              </ul>
     */
    public OptionalValue<InputValidationException> overridableCheckInput(CharacterCardInput input) {
        if (input.getTargetIsland().isEmpty()) {
            return OptionalValue.of(new InvalidElementException("Target Island")); // target ti not set
        }
        Island ti = input.getTargetIsland().get();
        int tiID = ti.getId();
        if (tiID < 0 || tiID >= 12) {
            return OptionalValue.of(new InvalidElementException("Target Island")); // target ti out of bounds for id
        }
        if (!this.context.getMutableIslandField().getMutableIslands().contains(ti)) {
            return OptionalValue.of(new InvalidElementException("Target Island")); // target ti not in field
        }
        return OptionalValue.empty();
    }

    /**
     * Refer to: {@link CharacterCard#unsafeApplyEffect(CharacterCardInput)} for further information
     */
    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        Island ti = input.getTargetIsland().get();
        for (IslandGroup ig : this.context.getMutableIslandField().getMutableGroups()) {
            if (ig.contains(ti)) {
                context.actMotherNaturePower(ig);
                return;
            }
        }
        throw new FailedOperationException("Card03.unsafeApplyEffect", "Target Island was not contained in any IslandGroup");
    }

    //test purpose only
}
