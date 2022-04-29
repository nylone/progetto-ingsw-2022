package it.polimi.ingsw.Model;

import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.io.Serializable;

public class EffectTracker implements Serializable {
    @Serial
    private static final long serialVersionUID = 135L; // convention: 1 for model, (01 -> 99) for objects
    private Optional<PawnColour> deniedPawnColour;
    private boolean denyTowerInfluence;
    private boolean increasedInfluence;
    private boolean increasedMotherNatureMovement;
    private boolean alternativeTeacherAssignmente;

    public EffectTracker() {
        this.reset();
    }

    public void reset() {
        this.deniedPawnColour = Optional.empty();
        this.denyTowerInfluence = false;
        this.increasedInfluence = false;
        this.increasedMotherNatureMovement = false;
        this.alternativeTeacherAssignmente = false;
    }

    public void enableIncreasedInfluence() {
        this.increasedInfluence = true;
    }

    public void enableDenyTowerInfluence() {
        this.denyTowerInfluence = true;
    }

    public void enableIncreasedMotherNatureMovement() {
        this.increasedMotherNatureMovement = true;
    }

    public void enableAlternativeTeacherAssignment() {
        this.alternativeTeacherAssignmente = true;
    }

    public boolean isInfluenceIncreased() {
        return increasedInfluence;
    }

    public boolean isTowerInfluenceDenied() {
        return denyTowerInfluence;
    }

    public boolean isMotherNatureMovementIncreased() {
        return increasedMotherNatureMovement;
    }

    public boolean isAlternativeTeacherAssignmentEnabled() {
        return alternativeTeacherAssignmente;
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
}