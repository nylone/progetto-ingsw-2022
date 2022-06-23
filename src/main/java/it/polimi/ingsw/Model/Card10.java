package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.Constants.CONTAINER_NAME_DININGROOM;
import static it.polimi.ingsw.Constants.INPUT_NAME_TARGET_PAWN_PAIRS;
import static it.polimi.ingsw.Misc.Utils.canMapFit;

/**
 * EFFECT: You may exchange up to 2 Students between your entrance and your Dining Room
 */
public class Card10 extends StatelessEffect {
    @Serial
    private static final long serialVersionUID = 112L; // convention: 1 for model, (01 -> 99) for objects

    public Card10(Model ctx) {
        super(10, 1, ctx);
    }

    /**
     * Refer to: {@link CharacterCard#overridableCheckInput(CharacterCardInput)} for further information
     * @param input CharacterCardInput should contain:
     *<ul>
     *  <li>A valid list of pair having following properties: </li>
     *              <ul>
     *                  No more than two pairs<br>
     *                  No null values inside pairs
     *              </ul>
     *  <li>Every pairs must follow this format:</li>
     *            <ul>
     *              first element from entrance and second from diningRoom
     *            </ul>
     *  <li>a valid PawnColour from card</li>
     * </ul>
     */
    @Override
    public boolean overridableCheckInput(CharacterCardInput input) throws InputValidationException {
        //convention of input.targetPawnPairs ---> array of pairs, first element is from entrance, second is from diningRoom
        Optional<List<Pair<PawnColour, PawnColour>>> optionalPawnPair = input.getTargetPawnPairs();
        // make sure that:
        if (
                optionalPawnPair.isEmpty() || // target pawn pairs was set as parameter
                        //optionalPawnPair.get().size() == 0 || // target pawn pairs is not empty (technically allowed)
                        optionalPawnPair.get().size() > 2 || // target pawn pairs are not over the pair limit of 2 swaps
                        optionalPawnPair.get().stream().anyMatch(p -> p.getFirst() == null || p.getSecond() == null) // no null values in pair
        ) {
            // in case throw exception for invalid element in input
            throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_PAIRS);
        }
        // explode pawnpairs into respective arrays of elements
        List<Pair<PawnColour, PawnColour>> pawnPairs = optionalPawnPair.get();
        // first count how many students of each colour the user picked
        Map<PawnColour, Integer> comingFromEntrance = new EnumMap<>(PawnColour.class); // counts user entrance selected colours
        Map<PawnColour, Integer> comingFromDiningRoom = new EnumMap<>(PawnColour.class); // counts diningroom selected colours
        for (Pair<PawnColour, PawnColour> pair : pawnPairs) {
            comingFromEntrance.merge(pair.getFirst(), 1, Integer::sum);
            comingFromDiningRoom.merge(pair.getSecond(), 1, Integer::sum);
        }

        // get user entrance counts per colour
        PlayerBoard playerBoard = input.getCaller();
        Map<PawnColour, Integer> entranceMap = new EnumMap<>(PawnColour.class); // counts user entrance total colours
        for (PawnColour pawn : playerBoard.getEntranceStudents().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList()) {
            entranceMap.merge(pawn, 1, Integer::sum);
        }

        // make sure the elements coming from user (first) are also mapped to entrance
        if (!canMapFit(entranceMap, comingFromEntrance)) {
            throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_PAIRS);
        }

        Map<PawnColour, Integer> diningRoomMap = new EnumMap<>(PawnColour.class); // counts user diningRoom total colours
        for (PawnColour pawn : PawnColour.values()) {
            diningRoomMap.merge(pawn, playerBoard.getDiningRoomCount(pawn), Integer::sum);
        }
        // make sure the elements coming from diningRoom (second) are also mapped to the diningroom
        if (!canMapFit(diningRoomMap, comingFromDiningRoom)) {
            throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_PAIRS);
        }

        // validate size of dining room
        for (PawnColour p : comingFromEntrance.keySet()) {
            if (playerBoard.getDiningRoomCount(p) - comingFromDiningRoom.getOrDefault(p, 0) + comingFromEntrance.getOrDefault(p, 0) > 10) {
                throw new GenericInputValidationException(CONTAINER_NAME_DININGROOM,
                        CONTAINER_NAME_DININGROOM + "can't contain " + pawnPairs.size()
                                + "elements without overflowing on one of its lanes.");
            }
        }

        return true; // all checks passed, return true
    }

    /**
     * Refer to: {@link CharacterCard#unsafeApplyEffect(CharacterCardInput)} for further information
     */
    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        // explode pawnpairs into respective arrays of elements
        List<Pair<PawnColour, PawnColour>> pawnPairs = input.getTargetPawnPairs().get();
        List<PawnColour> fromEntrance = new ArrayList<>(pawnPairs.size());
        List<PawnColour> fromDiningRoom = new ArrayList<>(pawnPairs.size());
        // get the playerboard to operate on
        PlayerBoard playerBoard = input.getCaller();
        for (Pair<PawnColour, PawnColour> p : pawnPairs) {
            fromEntrance.add(p.getFirst());
            fromDiningRoom.add(p.getSecond());
            playerBoard.removeStudentFromEntrance(p.getFirst());
            this.context.removeStudentFromDiningRoom(p.getSecond(), playerBoard);
        }
        // true effect happens here
        playerBoard.addStudentsToEntrance(fromDiningRoom);
        for (PawnColour student : fromEntrance) {
            this.context.addStudentToDiningRoom(student, playerBoard);
        }
    }

    //test purpose only
   /* @Override
    public String toString() {
        return "Card10{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }*/
}
