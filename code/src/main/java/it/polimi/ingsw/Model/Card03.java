package it.polimi.ingsw.Model;

/*
Choose an Island and resolve the Island as if
Mother Nature had ended her movement there. Mother
Nature will still move and the Island where she ends
her movement will also be resolved.
 */

import java.util.Optional;

public class Card03 extends StatelessEffect {
    public Card03(GameBoard ctx) {
        super(3, 3, ctx);
    }

    public int getId() {
        return this.id;
    }

    public int getCost() {
        return this.cost;
    }

    public int getTimeUsed() {
        return this.timeUsed;
    }

    public void Use(CharacterCardInput input) {
        Optional<IslandGroup> islandGroup = Optional.empty();
        for(IslandGroup ig : this.context.getIslandField().getGroups()){
            islandGroup = ig.find(input.getTargetIsland().get());
            if(islandGroup.isPresent()) break;
        }
        this.context.influencerOf(islandGroup.get());
        this.cost++;
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
