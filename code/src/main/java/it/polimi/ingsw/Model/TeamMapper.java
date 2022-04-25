package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.TeamID;
import it.polimi.ingsw.Model.Enums.TowerColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamMapper implements Serializable {
    @Serial
    private static final long serialVersionUID = 136L; // convention: 1 for model, (01 -> 99) for objects
    Map<PlayerBoard, TeamID> playerTeamMap;
    Map<TeamID, TowerStorage> towerStorageMap;

    public TeamMapper(List<PlayerBoard> players) {
        this.playerTeamMap = new HashMap<>();
        int nop = players.size();
        for (int i = 0; i < nop; i++) {
            this.playerTeamMap.put(players.get(i), TeamID.fromInteger(i % (nop == 4 ? 2 : nop)));
        } // note: for 4 players the first team is always made up by the first 2 nicknames
        this.towerStorageMap = new HashMap<>(); // creates tower storage associations based on number of players
        for (int i = 0; i < (nop == 4 ? 2 : nop); i++) {
            TeamID tID = TeamID.fromInteger(i);
            this.towerStorageMap.put(tID, new TowerStorage(TowerColour.fromTeamId(tID), nop == 3 ? 6 : 8));
        } // note: for 4 players the first team is always made up by the first 2 nicknames
    }

    public List<PlayerBoard> getPlayers(TeamID tID) {
        return playerTeamMap.entrySet().stream()
                .filter(e -> e.getValue().equals(tID))
                .map(Map.Entry::getKey)
                .toList();
    }

    public TeamID getTeamID(PlayerBoard pb) {
        return playerTeamMap.get(pb);
    }

    public TowerStorage getMutableTowerStorage(PlayerBoard pb) {
        return this.getMutableTowerStorage(this.getTeamID(pb));
    }

    public TowerStorage getMutableTowerStorage(TeamID tID) {
        return towerStorageMap.get(tID);
    }
}
