package it.polimi.ingsw.Model;

import java.util.Optional;

/*
When resolving a Conquering on an Island,
ed. ye Powers do not count towards influence.
 */
public class Card06 extends StatelessEffect{
    public Card06(GameBoard ctx) {
        super(6, 3, ctx);
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
        //todo
        context.getIslandField().getMotherNaturePosition().setDenyTowerInfluence(true);
        this.cost++;
    }

    //test purpose only
    @Override
    public String toString() {
        return "Card02{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
