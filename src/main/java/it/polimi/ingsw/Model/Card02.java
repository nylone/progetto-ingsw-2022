package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.util.Map;

/**
 * EFFECT: During this turn, you take control of any
 * number of Professors even if you have the same
 * number of Students as the player who currently controls them.
 */
public class Card02 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 104L; // convention: 1 for model, (01 -> 99) for objects

    public Card02(GameBoard ctx) {
        super(2, 2, ctx);
    }

    @Override
    public boolean overridableCheckInput(CharacterCardInput input) throws InputValidationException {
        return true;
    }

    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        Map<PawnColour, PlayerBoard> teachers = this.context.getTeachers();
        PlayerBoard me = input.getCaller();

        this.context.getMutableEffects().enableAlternativeTeacherAssignment();
        teachers.forEach((teacherColour, teacherOwner) -> {
            if (teacherOwner.getDiningRoomCount(teacherColour) == me.getDiningRoomCount(teacherColour))
                this.context.setTeacher(teacherColour, me);
        });
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