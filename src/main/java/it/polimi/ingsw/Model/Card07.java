package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import static it.polimi.ingsw.Constants.CONTAINER_NAME_ENTRANCE;
import static it.polimi.ingsw.Constants.INPUT_NAME_TARGET_PAWN_PAIRS;
import static it.polimi.ingsw.Misc.Utils.canCollectionFit;

/**
 * In Setup, draw 6 Students and place them on this card <br>
 * EFFECT: you may take up to 3 students from this card and replace them with the same number of Students
 * from your Entrance
 */
public class Card07 extends StatefulEffect {
    @Serial
    private static final long serialVersionUID = 109L; // convention: 1 for model, (01 -> 99) for objects

    private final PawnColour[] students = new PawnColour[6];

    public Card07(GameBoard ctx) {
        super(7, 1, StateType.PAWNCOLOUR, ctx);
        for (int i = 0; i < 6; i++) {
            this.students[i] = ctx.getMutableStudentBag().extract();
        }
    }

    public ArrayList<Object> getState() {
        return new ArrayList<>(Arrays.asList(students));
    }

    public StateType getStateType() {
        return stateType;
    }

    public boolean overridableCheckInput(CharacterCardInput input) throws InputValidationException {
        //convention of input.targetPawnPairs ---> array of pairs, first element is from entrance, second is from card
        Optional<Pair<PawnColour, PawnColour>[]> optionalPawnPair = input.getTargetPawnPairs();
        PlayerBoard playerBoard = input.getCaller();
        // make sure the pair is formatted properly
        if (
                optionalPawnPair.isEmpty() || // target pawn pairs was set as parameter
                        optionalPawnPair.get().length == 0 || // target pawn pairs is not empty
                        optionalPawnPair.get().length > 3 || // target pawn pairs are not over the pair limit of 2 swaps
                        Arrays.stream(optionalPawnPair.get()).anyMatch(p -> p.getFirst() == null || p.getSecond() == null) // no null values in pair
        ) {
            // in case throw exception for invalid element in input
            throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_PAIRS);
        }


        // explode pawnpairs into respective arrays of elements
        Pair<PawnColour, PawnColour>[] pawnPairs = optionalPawnPair.get();
        // first count how many students of each colour the user picked
        Map<PawnColour, Integer> firstMap = new EnumMap<>(PawnColour.class); // counts user entrance selected colours
        Map<PawnColour, Integer> secondMap = new EnumMap<>(PawnColour.class); // counts card state selected colours
        for (Pair<PawnColour, PawnColour> pair : pawnPairs) {
            firstMap.merge(pair.getFirst(), 1, Integer::sum);
            secondMap.merge(pair.getSecond(), 1, Integer::sum);
        }

        // get user entrance counts per colour
        PlayerBoard me = input.getCaller();
        Map<PawnColour, Integer> entranceMap = new EnumMap<>(PawnColour.class); // counts user entrance total colours
        for (PawnColour pawn : me.getEntranceStudents().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList()) {
            entranceMap.merge(pawn, 1, Integer::sum);
        }
        // make sure the elements coming from user (first) are also mapped to entrance
        if (!canCollectionFit(entranceMap, firstMap)) {
            throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_PAIRS);
        }

        // get card storage counts per colour
        Map<PawnColour, Integer> cardMap = new EnumMap<>(PawnColour.class); // counts user entrance total colours
        for (PawnColour pawn : this.students) {
            cardMap.merge(pawn, 1, Integer::sum);
        }
        // make sure the elements coming from card (second) are also mapped to the card state
        if (!canCollectionFit(cardMap, secondMap)) {
            throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_PAIRS);
        }
        if (playerBoard.getEntranceSpaceLeft()+pawnPairs.length >= input.getCaller().getEntranceSize()) {
            throw new GenericInputValidationException(CONTAINER_NAME_ENTRANCE,
                    CONTAINER_NAME_ENTRANCE + "does not contain " + pawnPairs.length
                            + "pawns");
        }

        return true;
    }

    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        PlayerBoard me = input.getCaller();

        //convention of input.targetPawnPairs ---> array of pairs, first element is from entrance, second is from card
        for (Pair<PawnColour, PawnColour> pair : input.getTargetPawnPairs().get()) {
            // match the first element in entrance with first and swap it with second
            me.removeStudentFromEntrance(pair.getFirst());
            me.addStudentToEntrance(pair.getSecond());

            // match the first element in card with second and swap it with first
            for (int i = 0; i < students.length; i++) {
                if (students[i] == pair.getSecond()) {
                    students[i] = pair.getFirst();
                    break;
                }
            }
        }
    }

    //test-purpose only
    @Override
    public String toString() {
        return "Card07{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}