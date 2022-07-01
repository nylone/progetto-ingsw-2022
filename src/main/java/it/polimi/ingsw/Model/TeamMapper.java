package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.TeamID;
import it.polimi.ingsw.Model.Enums.TowerColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maps the players in the model to their respective teams and {@link TowerStorage}
 */
public class TeamMapper implements Serializable {
    @Serial
    private static final long serialVersionUID = 136L; // convention: 1 for model, (01 -> 99) for objects
    final Map<PlayerBoard, TeamID> playerTeamMap;
    final Map<TeamID, TowerStorage> towerStorageMap;

    /**
     * Creates a new mapper. If the players are not 4, every player gets put into its own special team. If 4 players
     * are inputted, the first pair of players will be put in the first team, and the second pair into the second team.
     *
     * @param players a {@link List} of {@link PlayerBoard}s to put into teams.
     */
    public TeamMapper(List<PlayerBoard> players) {
        this.playerTeamMap = new HashMap<>();
        int nop = players.size();
        for (int i = 0; i < nop; i++) {
            this.playerTeamMap.put(players.get(i), TeamID.fromInteger(i % (nop == 4 ? 2 : nop)));
        } // note: for 4 players the first team is always made up by the even nicknames
        this.towerStorageMap = new HashMap<>(); // creates tower storage associations based on number of players
        for (int i = 0; i < (nop == 4 ? 2 : nop); i++) {
            TeamID tID = TeamID.fromInteger(i);
            this.towerStorageMap.put(tID, new TowerStorage(TowerColour.fromTeamId(tID), nop == 3 ? 6 : 8));
        }
    }

    /**
     * Get a team's players
     *
     * @param tID the ID of the Team to search players of
     * @return an Unmutable {@link List} containing references to the team's {@link PlayerBoard}s
     */
    public List<PlayerBoard> getMutablePlayers(TeamID tID) {
        return playerTeamMap.entrySet().stream()
                .filter(e -> e.getValue().equals(tID))
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Get a team's tower storage
     *
     * @param pb the player to find the {@link TowerStorage} of
     * @return a reference to {@link TowerStorage}, or null if the {@link PlayerBoard} matches no team in the game
     */
    public TowerStorage getMutableTowerStorage(PlayerBoard pb) {
        return this.getMutableTowerStorage(this.getTeamID(pb));
    }

    /**
     * Get a team's tower storage
     *
     * @param tID the ID of the Team to search the tower storage of
     * @return a reference to {@link TowerStorage}, or null if the TeamID is invalid
     */
    public TowerStorage getMutableTowerStorage(TeamID tID) {
        return towerStorageMap.get(tID);
    }

    /**
     * Get the team of a player
     *
     * @param pb the player to find the {@link TeamID} of
     * @return the {@link TeamID} of the player in input or null if the {@link PlayerBoard} matches no team in the game
     */
    public TeamID getTeamID(PlayerBoard pb) {
        return playerTeamMap.get(pb);
    }
}
