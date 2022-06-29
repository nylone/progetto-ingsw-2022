package it.polimi.ingsw.Model.Enums;

import java.io.Serial;
import java.io.Serializable;

/**
 * Towers are bound to a colour, and each colour is bound to a team. This enum represents the colour-team link
 */
public enum TowerColour implements Serializable {
    BLACK(TeamID.ONE),
    WHITE(TeamID.TWO),
    GRAY(TeamID.THREE);

    @Serial
    private static final long serialVersionUID = 132L; // convention: 1 for model, (01 -> 99) for objects
    private final TeamID teamID;

    /**
     * Internal constructor of the enum
     *
     * @param teamId the team to bind the tower to
     */
    TowerColour(TeamID teamId) {
        this.teamID = teamId;
    }

    /**
     * Get the {@link TowerColour} connected to a {@link TeamID}
     *
     * @param tID the {@link TeamID} enum representing a team
     * @return the {@link TowerColour} bound to a particular {@link TeamID}
     */
    public static TowerColour fromTeamId(TeamID tID) {
        for (TowerColour e : TowerColour.values()) {
            if (e.teamID == tID) return e;
        }
        throw new RuntimeException();
    }

    /**
     * Get the {@link TeamID} bound to a specific color of the tower
     *
     * @return the {@link TeamID} bound to the current {@link TowerColour} variant
     */
    public TeamID getTeamID() {
        return teamID;
    }
}
