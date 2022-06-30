package it.polimi.ingsw.Model;

import it.polimi.ingsw.Misc.OptionalValue;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.io.Serializable;

/**
 * {@link CharacterCard}s may impose some non immediate effects to resolve during the turn. This class aims to keep track of them
 */
public class EffectTracker implements Serializable {
    @Serial
    private static final long serialVersionUID = 135L; // convention: 1 for model, (01 -> 99) for objects
    private OptionalValue<PawnColour> deniedPawnColour;
    private boolean denyTowerInfluence;
    private boolean increasedInfluence;
    private boolean increasedMotherNatureMovement;
    private boolean alternativeTeacherAssignmente;

    /**
     * Constructor for the Tracker (all effects are disabled)
     */
    public EffectTracker() {
        this.reset();
    }

    /**
     * When called, this method will reset all effects to the disabled state.
     */
    public void reset() {
        this.deniedPawnColour = OptionalValue.empty();
        this.denyTowerInfluence = false;
        this.increasedInfluence = false;
        this.increasedMotherNatureMovement = false;
        this.alternativeTeacherAssignmente = false;
    }

    /**
     * enables the increased influence flag
     */
    public void enableIncreasedInfluence() {
        this.increasedInfluence = true;
    }

    /**
     * enables the denied tower influence flag
     */
    public void enableDenyTowerInfluence() {
        this.denyTowerInfluence = true;
    }

    /**
     * enables the increased mother nature movement flag
     */
    public void enableIncreasedMotherNatureMovement() {
        this.increasedMotherNatureMovement = true;
    }

    /**
     * enables the alternative teacher assignment algorithm flag
     */
    public void enableAlternativeTeacherAssignment() {
        this.alternativeTeacherAssignmente = true;
    }

    /**
     * @return value of the increased influence flag
     */
    public boolean isInfluenceIncreased() {
        return increasedInfluence;
    }

    /**
     * @return value of the denied tower influence flag
     */
    public boolean isTowerInfluenceDenied() {
        return denyTowerInfluence;
    }

    /**
     * @return value of the increased mother nature movement flag
     */
    public boolean isMotherNatureMovementIncreased() {
        return increasedMotherNatureMovement;
    }

    /**
     * @return value of the alternative teacher assignment algorithm flag
     */
    public boolean isAlternativeTeacherAssignmentEnabled() {
        return alternativeTeacherAssignmente;
    }

    /**
     * @return true if the value of the denied {@link PawnColour} is set
     */
    public boolean isPawnColourDenied() {
        return deniedPawnColour.isPresent();
    }

    /**
     * @return the denied {@link PawnColour} wrapped in a {@link OptionalValue}
     */
    public OptionalValue<PawnColour> getDeniedPawnColour() {
        return deniedPawnColour;
    }

    /**
     * sets the denied {@link PawnColour}
     *
     * @param pawnColour the {@link PawnColour} to be denied
     */
    public void setDeniedPawnColour(PawnColour pawnColour) {
        this.deniedPawnColour = OptionalValue.of(pawnColour);
    }
}