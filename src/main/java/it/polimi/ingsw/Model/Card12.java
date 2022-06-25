package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;

import java.io.Serial;

import static it.polimi.ingsw.Constants.INPUT_NAME_TARGET_PAWN_COLOUR;

/**
 * EFFECT: Choose a type of Student: every player
 * (including yourself) must return 3 Students of that type
 * from their Dining Room to the bag. If any player has
 * fewer than 3 Students of that type, return as many
 * Students as they have.
 */
public class Card12 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 114L; // convention: 1 for model, (01 -> 99) for objects

    public Card12(Model ctx) {
        super(12, 3, ctx);
    }

    /**
     * Refer to: {@link CharacterCard#overridableCheckInput(CharacterCardInput)} for further information
     *
     * @param input CharacterCardInput should contain:
     *              <ul>
     *               <li>A valid PawnColour</li>
     *              </ul>
     */
    public boolean overridableCheckInput(CharacterCardInput input) throws InputValidationException {
        if (input.getTargetPawn().isEmpty()) {
            throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_COLOUR);
        }
        return true;
    }

    /**
     * Refer to: {@link CharacterCard#unsafeApplyEffect(CharacterCardInput)} for further information
     */
    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        for (PlayerBoard p : this.context.getMutablePlayerBoards()) {
            //If any player has fewer than 3 Students of that type, return as many Students as they have.
            int amountToRemove = Math.min(3, p.getDiningRoomCount(input.getTargetPawn().get()));

            for (int i = 0; i < amountToRemove; i++) {
                this.context.removeStudentFromDiningRoom(input.getTargetPawn().get(), p);
            }

            for (int i = 0; i < amountToRemove; i++) {
                this.context.getMutableStudentBag().appendAndShuffle(input.getTargetPawn().get());
            }
        }
    }

    //test purpose only
   /* @Override
    public String toString() {
        return "Card12{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
    //*/
}
