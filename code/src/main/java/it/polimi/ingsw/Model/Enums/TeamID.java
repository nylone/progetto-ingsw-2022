package it.polimi.ingsw.Model.Enums;

import java.io.Serial;
import java.io.Serializable;

public enum TeamID implements Serializable {
    ONE(0), TWO(1), THREE(2);
    @Serial
    private static final long serialVersionUID = 137L; // convention: 1 for model, (01 -> 99) for objects
    private final int teamID;

    TeamID(int teamID) {
        this.teamID = teamID;
    }

    public static TeamID fromInteger(int id) {
        for (TeamID e : TeamID.values()) {
            if (e.getTeamID() == id) return e;
        }
        return null; // add exception if team id is not found
    }

    private int getTeamID() {
        return teamID;
    }

}
