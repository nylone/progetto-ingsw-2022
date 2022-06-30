package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Exceptions.Operation.FailedOperationException;
import it.polimi.ingsw.Misc.OptionalValue;
import it.polimi.ingsw.Model.Enums.StateType;

import java.io.Serial;
import java.util.ArrayList;

/**
 * In Setup, put the 4 No Entry tiles on this card.
 * EFFECT: Place a No Entrytile on an Island of your choice.
 * The first time Mother Nature ends her movement there, put the No Entry tile back onto this card
 * DO NOT calculate influence on that Island, or place any Towers.
 */
public class Card05 extends StatefulEffect {
    @Serial
    private static final long serialVersionUID = 107L; // convention: 1 for model, (01 -> 99) for objects

    //List containing card's tiles
    private final ArrayList<NoEntryTile> tiles;

    public Card05(Model ctx) {
        super(5, 2, StateType.NOENTRY, ctx);
        tiles = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            tiles.add(new NoEntryTile(this));
        }
    }

    /**
     * Get card's content
     *
     * @return ArrayList of Objects with noEntryTile (Can be casted to {@link NoEntryTile})
     */
    public ArrayList<Object> getState() {
        return new ArrayList<>(tiles);
    }

    /**
     * Get card's stateType
     *
     * @return card's stateType
     */
    public StateType getStateType() {
        return stateType;
    }

    /**
     * Refer to: {@link CharacterCard#overridableCheckInput(CharacterCardInput)} for further information
     *
     * @param input CharacterCardInput should contain:
     *              <ul>
     *               <li>A valid island's ID </li>
     *              </ul>
     */
    public OptionalValue<InputValidationException> overridableCheckInput(CharacterCardInput input) {
        if (input.getTargetIsland().isEmpty()) {
            return OptionalValue.of(new InvalidElementException("Target Island")); // target ti not set
        }
        Island ti = input.getTargetIsland().get();
        if (ti.getId() < 0 || ti.getId() >= 12) {
            return OptionalValue.of(new InvalidElementException("Target Island")); // target ti out of bounds for id
        }
        if (!this.context.getMutableIslandField().getMutableIslands().contains(ti)) {
            return OptionalValue.of(new InvalidElementException("Target Island")); // target ti not in field
        } // note: if island is in field then the island must also be in a group, due to how islandfield works.
        if (tiles.size() == 0) {
            return OptionalValue.of(new GenericInputValidationException("Card05",
                    "has finished all its NoEntryTile(s)"));
        }
        //all tests passed
        return OptionalValue.empty();
    }

    /**
     * Refer to: {@link CharacterCard#unsafeApplyEffect(CharacterCardInput)} for further information
     */
    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception {
        Island ti = input.getTargetIsland().get();
        for (IslandGroup ig : this.context.getMutableIslandField().getMutableGroups()) {
            if (ig.contains(ti)) {
                ig.addNoEntry(tiles.remove(0));
                return;
            }
        }
        throw new FailedOperationException("Card05.unsafeApplyEffect", "Target Island was not contained in any IslandGroup");
    }

    /**
     * Add NoEntryTile to card
     *
     * @param tile tile to add
     */
    public void tileReset(NoEntryTile tile) {
        this.tiles.add(tile);
    }

    //test-purpose only
    /*@Override
    public String toString() {
        return "Card05{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
    //*/
}
