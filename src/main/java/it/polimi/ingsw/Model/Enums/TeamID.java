package it.polimi.ingsw.Model.Enums;

import java.io.Serial;
import java.io.Serializable;

/**
 * There are only a handful of possible teams in a game. This enumeration represents them
 */
public enum TeamID implements Serializable {
    ONE(0), TWO(1), THREE(2);
    @Serial
    private static final long serialVersionUID = 137L; // convention: 1 for model, (01 -> 99) for objects
    private final int teamID;

    /**
     * Internal constructor of the enum
     * @param teamID the id of the team
     */
    TeamID(int teamID) {
        this.teamID = teamID;
    }

    /**
     * Given an integer, find the corresponding team
     * @param id the id of the team to enumerate
     * @return the {@link TeamID} enum variant if the id is between 0 and 3 (excluded), otherwise returns null
     */
    public static TeamID fromInteger(int id) {
        for (TeamID e : TeamID.values()) {
            if (e.teamID == id) return e;
        }
        return null;
    }
}
