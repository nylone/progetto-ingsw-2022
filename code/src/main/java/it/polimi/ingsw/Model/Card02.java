package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;

/*
During this turn, you take control of any
number of Professors even if you have the same
number of Students as the player who currentlly controls them.
 */
public class Card02 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 104L; // convention: 1 for model, (01 -> 99) for objects

    public Card02(GameBoard ctx) {
        super(2, 2, ctx);
    }

    public boolean checkInput(CharacterCardInput input) {
        //todo
    }

    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        for (PawnColour pawnColour : PawnColour.values()) {
            for (PlayerBoard p : context.getPlayerBoards()) {
                if (p.equals(input.getCaller())) continue;
                if (p.getDiningRoomCount(pawnColour) == input.getCaller().getDiningRoomCount(pawnColour)
                        && p.getDiningRoomCount(pawnColour) != 0) {
                    context.setTeacher(pawnColour, input.getCaller());
                }
            }
        }
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
