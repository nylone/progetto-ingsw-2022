package it.polimi.ingsw.Model;

import it.polimi.ingsw.Misc.OptionalValue;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Each {@link CharacterCard} can use many input parameters. This class serves as a way to group all possible inputs and
 * edit them in a safe, coherent way.
 */
public class CharacterCardInput implements Serializable {
    @Serial
    private static final long serialVersionUID = 116L; // convention: 1 for model, (01 -> 99) for objects
    private final PlayerBoard caller;
    private Island targetIsland;
    private PawnColour targetPawn;
    private List<Pair<PawnColour, PawnColour>> targetPawnPairs;

    /**
     * Constructor for the base input of the card.
     *
     * @param caller each card requires the caller's {@link PlayerBoard} as an input.
     */
    public CharacterCardInput(PlayerBoard caller) {
        this.caller = caller;
    }

    /**
     * @return the caller's {@link PlayerBoard}
     */
    public PlayerBoard getCaller() {
        return caller;
    }

    /**
     * @return the {@link Island} set as a target. The value is wrapped in a {@link OptionalValue}, as the related input is not
     * compulsory
     */
    public OptionalValue<Island> getTargetIsland() {
        if (this.targetIsland == null) return OptionalValue.empty();
        else return OptionalValue.of(targetIsland);
    }

    /**
     * @param targetIsland the {@link Island} to set as a target
     */
    public void setTargetIsland(Island targetIsland) {
        this.targetIsland = targetIsland;
    }

    /**
     * @return the {@link PawnColour} set as a target. The value is wrapped in a {@link OptionalValue}, as the related input is not
     * compulsory
     */
    public OptionalValue<PawnColour> getTargetPawn() {
        if (this.targetPawn == null) return OptionalValue.empty();
        else return OptionalValue.of(targetPawn);
    }

    /**
     * @param targetPawn the {@link PawnColour} to set as a target
     */
    public void setTargetPawn(PawnColour targetPawn) {
        this.targetPawn = targetPawn;
    }

    /**
     * @return the the {@link List} of {@link Pair}s of {@link PawnColour} set as a target.
     * The value is wrapped in a {@link OptionalValue}, as the related input is not
     * compulsory
     */
    public OptionalValue<List<Pair<PawnColour, PawnColour>>> getTargetPawnPairs() {
        if (this.targetPawnPairs == null) return OptionalValue.empty();
        else return OptionalValue.of(targetPawnPairs);
    }

    /**
     * Note: the convention of the {@link Pair} is to be verified through the card that requires the input.
     *
     * @param targetPawnPairs the {@link List} of {@link Pair}s of {@link PawnColour} to set as a target.
     */
    public void setTargetPawnPairs(List<Pair<PawnColour, PawnColour>> targetPawnPairs) {
        this.targetPawnPairs = targetPawnPairs;
    }
}
