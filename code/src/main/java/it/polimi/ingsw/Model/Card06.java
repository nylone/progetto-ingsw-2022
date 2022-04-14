package it.polimi.ingsw.Model;

import java.io.Serial;

/*
When resolving a Conquering on an Island, Towers do not count towards influence.
 */
public class Card06 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 108L; // convention: 1 for model, (01 -> 99) for objects

    public Card06(GameBoard ctx) {
        super(6, 3, ctx);
    }

    public void Use(CharacterCardInput input) {
        context.effects.denyIslandGroup(context.getIslandField().getMotherNaturePosition());
        addUse();
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
