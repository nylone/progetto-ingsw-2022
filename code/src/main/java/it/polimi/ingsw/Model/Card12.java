package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.EmptyDiningRoomException;
import it.polimi.ingsw.Exceptions.InvalidInputException;

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
        if(input.getTargetPawn().isPresent()) {
            int pawn_to_remove = 0;
            for (PlayerBoard p : this.context.getPlayerBoards()) {
                pawn_to_remove += Math.min(3, p.getDiningRoomCount(input.getTargetPawn().get())); //If any player has fewer than 3 Students of that type, return as many Students as they have.
                try {
                    p.removeStudentFromDiningRoom(input.getTargetPawn().get(), pawn_to_remove);
                    for (int i = 0; i < pawn_to_remove; i++) {
                        this.context.getStudentBag().appendAndShuffle(input.getTargetPawn().get());
                    }
                    pawn_to_remove=0;
                } catch (EmptyDiningRoomException e) {
                    e.printStackTrace();
                }
            }
            addUse();
        }else{
            throw new InvalidInputException();
        }
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
