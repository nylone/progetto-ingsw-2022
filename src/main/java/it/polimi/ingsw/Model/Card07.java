package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Misc.OptionalValue;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;

import java.io.Serial;
import java.util.*;

import static it.polimi.ingsw.Misc.Utils.canMapFit;

/**
 * In Setup, draw 6 Students and place them on this card <br>
 * EFFECT: you may take up to 3 students from this card and replace them with the same number of Students
 * from your Entrance
 */
public class Card07 extends StatefulEffect {
    @Serial
    private static final long serialVersionUID = 109L; // convention: 1 for model, (01 -> 99) for objects

    private final PawnColour[] students = new PawnColour[6];

    public Card07(Model ctx) {
        super(7, 1, StateType.PAWNCOLOUR, ctx);
        for (int i = 0; i < 6; i++) {
            try {
                this.students[i] = ctx.getMutableStudentBag().extract();
            } catch (EmptyContainerException e) {
                // should never happen
                Logger.severe("student bag was found empty while adding a student to Card07. Critical, unrecoverable, error");
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public ArrayList<Object> getState() {
        return new ArrayList<>(Arrays.asList(students));
    }

    @Override
    public StateType getStateType() {
        return stateType;
    }

    /**
     * Refer to: {@link CharacterCard#overridableCheckInput(CharacterCardInput)} for further information
     *
     * @param input CharacterCardInput should contain:
     *              <ul>
     *               <li>A valid list of pair having following properties: </li>
     *                           <ul>
     *                               No more than three pairs<br>
     *                               No null values inside pairs
     *                           </ul>
     *               <li>Every pairs must follow this format:</li>
     *                         <ul>
     *                           first element from entrance and second from card
     *                         </ul>
     *               <li>a valid PawnColour from card</li>
     *              </ul>
     */
    public OptionalValue<InputValidationException> overridableCheckInput(CharacterCardInput input) {
        //convention of input.targetPawnPairs ---> array of pairs, first element is from entrance, second is from card
        OptionalValue<List<Pair<PawnColour, PawnColour>>> optionalPawnPair = input.getTargetPawnPairs();
        PlayerBoard playerBoard = input.getCaller();
        // make sure the pair is formatted properly
        if (
                optionalPawnPair.isEmpty() || // target pawn pairs was set as parameter
                        //optionalPawnPair.get().size() == 0 || /target pawn pairs is not empty (technically allowed)
                        optionalPawnPair.get().size() > 3 || // target pawn pairs are not over the pair limit of 2 swaps
                        optionalPawnPair.get().stream().anyMatch(p -> p.first() == null || p.second() == null) // no null values in pair
        ) {
            // in case throw exception for invalid element in input
            return OptionalValue.of(new InvalidElementException("Target Pawn Pairs"));
        }


        // explode pawnpairs into respective arrays of elements
        List<Pair<PawnColour, PawnColour>> pawnPairs = optionalPawnPair.get();
        // first count how many students of each colour the user picked
        Map<PawnColour, Integer> firstMap = new EnumMap<>(PawnColour.class); // counts user entrance selected colours
        Map<PawnColour, Integer> secondMap = new EnumMap<>(PawnColour.class); // counts card state selected colours
        for (Pair<PawnColour, PawnColour> pair : pawnPairs) {
            firstMap.merge(pair.first(), 1, Integer::sum);
            secondMap.merge(pair.second(), 1, Integer::sum);
        }

        // get user entrance counts per colour
        PlayerBoard me = input.getCaller();
        Map<PawnColour, Integer> entranceMap = new EnumMap<>(PawnColour.class); // counts user entrance total colours
        for (PawnColour pawn : me.getEntranceStudents().stream()
                .filter(OptionalValue::isPresent)
                .map(OptionalValue::get)
                .toList()) {
            entranceMap.merge(pawn, 1, Integer::sum);
        }
        // make sure the elements coming from user (first) are also mapped to entrance
        if (!canMapFit(entranceMap, firstMap)) {
            return OptionalValue.of(new InvalidElementException("Target Pawn Pairs"));
        }

        // get card storage counts per colour
        Map<PawnColour, Integer> cardMap = new EnumMap<>(PawnColour.class); // counts user entrance total colours
        for (PawnColour pawn : this.students) {
            cardMap.merge(pawn, 1, Integer::sum);
        }
        // make sure the elements coming from card (second) are also mapped to the card state
        if (!canMapFit(cardMap, secondMap)) {
            return OptionalValue.of(new InvalidElementException("Target Pawn Pairs"));
        }
        if (playerBoard.getEntranceSpaceLeft() + pawnPairs.size() >= playerBoard.getEntranceSize()) {
            return OptionalValue.of(new GenericInputValidationException("Entrance",
                    "does not contain " + pawnPairs.size()
                            + " pawns"));
        }
        if (context.getMutableStudentBag().getSize() == 0) {
            return OptionalValue.of(new GenericInputValidationException("Student Bag", "is empty"));
        }

        return OptionalValue.empty();
    }

    /**
     * Refer to: {@link CharacterCard#unsafeApplyEffect(CharacterCardInput)} for further information
     */
    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        PlayerBoard me = input.getCaller();

        //convention of input.targetPawnPairs ---> array of pairs, first element is from entrance, second is from card
        for (Pair<PawnColour, PawnColour> pair : input.getTargetPawnPairs().get()) {
            // match the first element in entrance with first and swap it with second
            me.removeStudentFromEntrance(pair.first());
            me.addStudentToEntrance(pair.second());

            // match the first element in card with second and swap it with first
            for (int i = 0; i < students.length; i++) {
                if (students[i] == pair.second()) {
                    students[i] = pair.first();
                    break;
                }
            }
        }
    }

    //test-purpose only
}
