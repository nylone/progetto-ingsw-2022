package it.polimi.ingsw.Model;

/*
Choose an Island and resolve the Island as if
Mother Nature had ended her movement there. Mother
Nature will still move and the Island where she ends
her movement will also be resolved.
 */

import it.polimi.ingsw.Exceptions.InputValidationException;
import it.polimi.ingsw.Exceptions.InvalidElementException;
import it.polimi.ingsw.Exceptions.toremove.InvalidInputException;

import java.io.Serial;

import static it.polimi.ingsw.Constants.INPUT_NAME_TARGET_ISLAND;

public class Card03 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 105L; // convention: 1 for model, (01 -> 99) for objects

    public Card03(GameBoard ctx) {
        super(3, 3, ctx);
    }

    public boolean checkInput(CharacterCardInput input) throws InputValidationException {
        if(input.getTargetPawn().isEmpty()){
            throw new InvalidElementException(INPUT_NAME_TARGET_ISLAND);
        }
        return true;
    }

    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        Island ti = input.getTargetIsland().get();
        for (IslandGroup ig : this.context.getIslandField().getGroups()) {
            if (ig.contains(ti)) {
                context.actMotherNaturePower(ig);
                break;
            }
        }

    }

    //test purpose only
    @Override
    public String toString() {
        return "Card03{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
