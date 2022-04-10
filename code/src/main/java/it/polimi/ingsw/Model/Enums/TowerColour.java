package it.polimi.ingsw.Model.Enums;

import java.io.Serial;
import java.io.Serializable;

public enum TowerColour implements Serializable {
    BLACK(TeamID.ONE),
    WHITE(TeamID.TWO),
    GRAY(TeamID.THREE);

    private final TeamID teamID;
    @Serial
    private static final long serialVersionUID = 132L; // convention: 1 for model, (01 -> 99) for objects

    TowerColour(TeamID teamId) {
        this.teamID = teamId;
    }

    public static TowerColour fromTeamId(TeamID tID) {
        for (TowerColour e : TowerColour.values()) {
            if (e.getTeamID() == tID) return e;
        }
        return null; // add exception if team id is not found
    }

    public TeamID getTeamID() {
        return teamID;
    }
}
