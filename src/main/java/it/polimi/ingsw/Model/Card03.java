package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Exceptions.Operation.FailedOperationException;

import java.io.Serial;

import static it.polimi.ingsw.Constants.INPUT_NAME_TARGET_ISLAND;
import static it.polimi.ingsw.Constants.OPERATION_NAME_CARD03_APPLY_EFFECT;

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
     * @param input CharacterCardInput should contain:
     *<ul>
     *  <li>A valid island's ID </li>
     * </ul>
     */
    public boolean overridableCheckInput(CharacterCardInput input) throws InputValidationException {
        if (input.getTargetIsland().isEmpty()) {
            throw new InvalidElementException(INPUT_NAME_TARGET_ISLAND); // target ti not set
        }
        Island ti = input.getTargetIsland().get();
        if (ti.getId() < 0 || ti.getId() >= 12) {
            throw new InvalidElementException(INPUT_NAME_TARGET_ISLAND); // target ti out of bounds for id
        }
        if (!this.context.getMutableIslandField().getMutableIslands().contains(ti)) {
            throw new InvalidElementException(INPUT_NAME_TARGET_ISLAND); // target ti not in field
        }
        return true;
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
        throw new FailedOperationException(OPERATION_NAME_CARD03_APPLY_EFFECT, "Target Island was not contained in any IslandGroup");
    }

    //test purpose only
    /*@Override
    public String toString() {
        return "Card03{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }*/
}
