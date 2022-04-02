package it.polimi.ingsw.Model;

import java.io.Serial;
import java.io.Serializable;

public enum TowerColour implements Serializable {
    BLACK(0),
    WHITE(1),
    GRAY(2);

    private final int teamId;
    @Serial
    private static final long serialVersionUID = 132L; // convention: 1 for model, (01 -> 99) for objects

    TowerColour(int teamId) {
        this.teamId = teamId;
    }

    public static TowerColour fromTeamId(int id) {
        for (TowerColour e : TowerColour.values()) {
            if (e.getTeamId() == id) return e;
        }
        return null; // add exception if team id is not found
    }

    public int getTeamId() {
        return teamId;
    }
}
