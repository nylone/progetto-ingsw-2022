package it.polimi.ingsw.Model;

import java.util.Optional;

public class CharacterCardInput {
    private Island targetIsland;
    private PawnColour targetPawn;
    private PawnColour[][] targetPawnPairs;
    private PlayerBoard caller;

    public CharacterCardInput(PlayerBoard caller) {
        this.caller = caller;
        targetPawnPairs = new PawnColour[2][];
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

    public Optional<PawnColour[][]> getTargetPawnPairs() {
        if(this.targetPawnPairs==null) return Optional.empty();
        else return Optional.of(targetPawnPairs);
    }


    public void setTargetIsland(Island targetIsland) {
        this.targetIsland = targetIsland;
    }

    public void setTargetPawn(PawnColour targetPawn) {
        this.targetPawn = targetPawn;
    }

    public void setTargetPawnPairs(PawnColour[][] targetPawnPairs) {
        this.targetPawnPairs = targetPawnPairs;
    }
}
