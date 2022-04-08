package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.EmptyDiningRoomException;

import java.io.Serial;

/*
EFFECT: Choose a type of Student: every player
(including yourself) must return 3 Students of that type
from their Dining Room to the bag. If any player has
fewer than 3 Students of that type, return as many
Students as they have.
 */
public class Card12 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 114L; // convention: 1 for model, (01 -> 99) for objects

    public Card12(GameBoard ctx) {
        super(12, 3, ctx);
    }

    public void Use(CharacterCardInput input) {
        //todo
        int pawn_to_remove = 0;
        for(PlayerBoard p : context.getPlayerBoards()){
            pawn_to_remove += Math.min(3, p.getDiningRoomCount(input.getTargetPawn().get()));
            try {
                input.getCaller().removeStudentFromDiningRoom(input.getTargetPawn().get(), Math.max(0, p.getDiningRoomCount(input.getTargetPawn().get())) -3); //assume to remove a consistent quantity of pawns (avoid negative quantity of students in diningroom)
            } catch (EmptyDiningRoomException e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<pawn_to_remove; i++){
            context.getStudentBag().appendAndShuffle(input.getTargetPawn().get());
        }
        addUse();
    }

    //test purpose only
    @Override
    public String toString() {
        return "Card12{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
