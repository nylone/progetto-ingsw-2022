package it.polimi.ingsw.Model.Enums;

public enum TeamID {
    ONE(0), TWO(1), THREE(2);
    private final int teamID;
    TeamID(int teamID) {this.teamID = teamID;}

    public static TeamID fromInteger(int id) {
        for (TeamID e : TeamID.values()) {
            if (e.getTeamID() == id) return e;
        }
        return null; // add exception if team id is not found
    }

    private int getTeamID() {return teamID;}

}
