package it.polimi.ingsw.Model;

/*
Choose an Island and resolve the Island as if
Mother Nature had ended her movement there. Mother
Nature will still move and the Island where she ends
her movement will also be resolved.
 */

import it.polimi.ingsw.Exceptions.InvalidInputException;

import java.io.Serial;
import java.util.Optional;

public class Card03 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 105L; // convention: 1 for model, (01 -> 99) for objects

    public Card03(GameBoard ctx) {
        super(3, 3, ctx);
    }

    public void Use(CharacterCardInput input) {
        Island ti = input.getTargetIsland().orElseThrow(InvalidInputException::new);
        for(IslandGroup ig : this.context.getIslandField().getGroups()){
            Optional<IslandGroup> tg = ig.find(ti); // Islandgroup containing targetIsland
            if (tg.isPresent()) {
                context.actMotherNaturePower(tg.get());
                break;
            }
        }
        addUse();
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
