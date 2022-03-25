package it.polimi.ingsw.Model;

public enum TowerColour {
    BLACK(0),
    WHITE(1),
    GRAY(2);

    private final int teamId;

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
