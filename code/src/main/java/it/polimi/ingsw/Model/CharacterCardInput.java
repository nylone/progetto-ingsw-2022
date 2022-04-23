package it.polimi.ingsw.Model;

import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.util.Optional;

public class CharacterCardInput {
    private static final long serialVersionUID = 116L; // convention: 1 for model, (01 -> 99) for objects

    private Island targetIsland;
    private PawnColour targetPawn;
    private Pair<PawnColour, PawnColour>[] targetPawnPairs;
    private final PlayerBoard caller;

    public CharacterCardInput(PlayerBoard caller) {
        this.caller = caller;
    }

    public PlayerBoard getCaller() {
        return caller;
    }
    public Optional<Island> getTargetIsland() {
        if(this.targetIsland==null) return Optional.empty();
        else return Optional.of(targetIsland);
    }

    public Optional<PawnColour> getTargetPawn() {
        if(this.targetPawn==null) return Optional.empty();
        else return Optional.of(targetPawn);
    }

    public Optional<Pair<PawnColour, PawnColour>[]> getTargetPawnPairs() {
        if(this.targetPawnPairs==null) return Optional.empty();
        else return Optional.of(targetPawnPairs);
    }


    public void setTargetIsland(Island targetIsland) {
        this.targetIsland = targetIsland;
    }

    public void setTargetPawn(PawnColour targetPawn) {
        this.targetPawn = targetPawn;
    }

    public void setTargetPawnPairs(Pair<PawnColour, PawnColour>[] targetPawnPairs) {
        this.targetPawnPairs = targetPawnPairs;
    }
}
