package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.PawnColour;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EffectTracker {
    private Optional<PawnColour> deniedPawnColour;
    private boolean denyPawnColourIsActive;
    private boolean denyTowerInfluence;
    private boolean increasedInfluenceIsActive;

    public EffectTracker() {
        deniedPawnColour = Optional.empty();
        denyPawnColourIsActive = false;
        this.denyTowerInfluence = false;
        increasedInfluenceIsActive = false;
    }

    public boolean getDenyPawnColour() {
        return denyPawnColourIsActive;
    }

    public Optional<PawnColour> getDeniedPawnColour() {
        return deniedPawnColour;
    }

    public void setDeniedPawnColour(boolean denyPawnColourIsActive, PawnColour colour) {
        if (!denyPawnColourIsActive) {
            this.deniedPawnColour = Optional.empty();
        }else {
            this.denyPawnColourIsActive = true;
            this.deniedPawnColour = Optional.of(colour);
        }
    }

    public boolean getIncreasedInfluenceIsActive() {
        return increasedInfluenceIsActive;
    }

    public boolean getDenyTowerInfluence() {
        return denyTowerInfluence;
    }

    public void setIncreasedInfluenceIsActive(boolean increasedInfluenceIsActive) {
        this.increasedInfluenceIsActive = increasedInfluenceIsActive;
    }

    public void setDenyTowerInfluence(boolean b) {
        this.denyTowerInfluence = b;
    }

}