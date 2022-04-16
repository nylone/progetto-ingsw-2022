package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.PawnColour;

import java.util.Optional;

public class EffectTracker {
    private Optional<PawnColour> deniedPawnColour;
    private boolean denyTowerInfluence;
    private boolean increasedInfluence;

    public EffectTracker() {
        this.deniedPawnColour = Optional.empty();
        this.denyTowerInfluence = false;
        this.increasedInfluence = false;
    }

    public void enableIncreasedInfluence() {
        this.increasedInfluence = true;
    }

    public void enableDenyTowerInfluence() {
        this.denyTowerInfluence = true;
    }

    public boolean isInfluenceIncreased() {
        return increasedInfluence;
    }

    public boolean isTowerInfluenceDenied() {
        return denyTowerInfluence;
    }

    public boolean isPawnColourDenied() {
        return deniedPawnColour.isPresent();
    }

    public Optional<PawnColour> getDeniedPawnColour() {
        return deniedPawnColour;
    }

    public void setDeniedPawnColour(PawnColour pawnColour) {
        this.deniedPawnColour = Optional.of(pawnColour);
    }

    public void reset() {
        this.deniedPawnColour = Optional.empty();
        this.denyTowerInfluence = false;
        this.increasedInfluence = false;
    }
}