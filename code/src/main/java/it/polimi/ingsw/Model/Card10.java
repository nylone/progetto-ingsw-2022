package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.Constants.*;

/**
 * EFFECT: You may exchange up to 2 Students between your entrance and your Dining Room
 */
public class Card10 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 112L; // convention: 1 for model, (01 -> 99) for objects

    public Card10(GameBoard ctx) {
        super(10, 1, ctx);
    }

    @Override
    public boolean overridableCheckInput(CharacterCardInput input) throws InputValidationException {
        //convention of input.targetPawnPairs ---> array of pairs, first element is from entrance, second is from diningroom
        Optional<Pair<PawnColour, PawnColour>[]> optionalPawnPair = input.getTargetPawnPairs();
        // make sure that:
        if (
                optionalPawnPair.isEmpty() || // target pawn pairs was set as parameter
                        optionalPawnPair.get().length == 0 || // target pawn pairs is not empty
                        optionalPawnPair.get().length > 2 || // target pawn pairs are not over the pair limit of 2 swaps
                        Arrays.stream(optionalPawnPair.get()).anyMatch(p -> p.getFirst() == null || p.getSecond() == null) // no null values in pair
        ) {
            // in case throw exception for invalid element in input
            throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_PAIRS);
        }
        // explode pawnpairs into respective arrays of elements
        Pair<PawnColour, PawnColour>[] pawnPairs = optionalPawnPair.get();
        List<PawnColour> fromEntrance = new ArrayList<>(pawnPairs.length);
        // get the playerboard to operate on
        for (Pair<PawnColour, PawnColour> p : pawnPairs) {
            fromEntrance.add(p.getFirst());
        }
        PlayerBoard playerBoard = input.getCaller();

        // validate size of entrance
        if (playerBoard.getEntranceSpaceLeft() < pawnPairs.length) {
            throw new GenericInputValidationException(CONTAINER_NAME_ENTRANCE,
                    CONTAINER_NAME_ENTRANCE + "can't contain " + pawnPairs.length
                            + " element's without overflowing.");
        }
        // validate size of dining room
        if (!playerBoard.canDiningRoomFit(fromEntrance)) {
            throw new GenericInputValidationException(CONTAINER_NAME_DININGROOM,
                    CONTAINER_NAME_DININGROOM + "can't contain " + pawnPairs.length
                            + "elements without overflowing on one of its lanes.");
        }
        return true; // all checks passed, return true
    }

    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        // explode pawnpairs into respective arrays of elements
        Pair<PawnColour, PawnColour>[] pawnPairs = input.getTargetPawnPairs().get();
        List<PawnColour> fromEntrance = new ArrayList<>(pawnPairs.length);
        List<PawnColour> fromDiningRoom = new ArrayList<>(pawnPairs.length);
        for (Pair<PawnColour, PawnColour> p : pawnPairs) {
            fromEntrance.add(p.getFirst());
            fromDiningRoom.add(p.getSecond());
        }
        // get the playerboard to operate on
        PlayerBoard playerBoard = input.getCaller();

        // true effect happens here
        playerBoard.addStudentsToEntrance(fromDiningRoom);
        for (PawnColour student : fromEntrance) {
            playerBoard.addStudentToDiningRoom(student);
        }
    }

    //test purpose only
    @Override
    public String toString() {
        return "Card10{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
