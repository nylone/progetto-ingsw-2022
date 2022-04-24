package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InputValidationException;
import it.polimi.ingsw.Exceptions.InvalidElementException;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;

import static it.polimi.ingsw.Constants.INPUT_NAME_CALLER;

/*
During this turn, you take control of any
number of Professors even if you have the same
number of Students as the player who currently controls them.
 */
public class Card02 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 104L; // convention: 1 for model, (01 -> 99) for objects

    public Card02(GameBoard ctx) {
        super(2, 2, ctx);
    }

    public boolean checkInput(CharacterCardInput input) throws InputValidationException {
        if (input.getCaller() == null) {
            throw new InvalidElementException(INPUT_NAME_CALLER);
        }
        return true;
    }

    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        // todo this implementation is wrong
        for (PawnColour pawnColour : PawnColour.values()) {
            for (PlayerBoard p : context.getPlayerBoards()) {
                if (p.equals(input.getCaller())) continue;
                // todo we take control of a teacher if we and another player have the same number of students
                // this should be checked only on players with majority on a teacher
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
