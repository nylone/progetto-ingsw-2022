package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Operation.*;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.TowerColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.Constants.OPERATION_NAME_CONSTRUCTOR_ISLAND_GROUPS;

public class IslandGroup implements Serializable {
    @Serial
    private static final long serialVersionUID = 123L; // convention: 1 for model, (01 -> 99) for objects

    private final int id;
    private final ArrayList<Island> islands;
    private Stack<NoEntryTile> noEntryTiles;

    public IslandGroup(Island i) {
        this.islands = new ArrayList<>();
        this.id = i.getId();
        this.islands.add(i);
        this.noEntryTiles = new Stack<>();
    }

    public IslandGroup(IslandGroup... islandGroups) throws OperationException {
        if (islandGroups.length > 0) {
            if (islandGroups[0].isJoinable(islandGroups)) {
                this.islands = new ArrayList<>();
                for (IslandGroup i : islandGroups) {
                    islands.addAll(i.getMutableIslands());
                }
                this.id = Arrays.stream(islandGroups)
                        .min(Comparator.comparingInt(IslandGroup::getId))
                        .orElseThrow(() -> new FailedOperationException(OPERATION_NAME_CONSTRUCTOR_ISLAND_GROUPS))
                        .getId();
            } else {
                throw new ForbiddenOperationException(OPERATION_NAME_CONSTRUCTOR_ISLAND_GROUPS);
            }
        } else {
            throw new ForbiddenOperationException(OPERATION_NAME_CONSTRUCTOR_ISLAND_GROUPS);
        }
    }

    // returns true if the inputted IslandGroups all contain the same type of tower
    public boolean isJoinable(IslandGroup... groups) {
        if (groups.length <= 0) {
            return false;
        } else {
            if (this.getTowerColour().isEmpty()) return false;
            else return
                    Arrays.stream(groups).allMatch(g -> g.getTowerColour().equals(this.getTowerColour()));
        }
    }

    public Optional<TowerColour> getTowerColour() {
        return this.getMutableIslands().get(0).getTowerColour();
    }

    public int getTowerCount() {
        if (getTowerColour().isPresent()) return getMutableIslands().size();
        else return 0;
    }

    public int getId() {
        return id;
    }

    public List<Island> getMutableIslands() {
        return List.copyOf(islands);
    }

    public List<NoEntryTile> getMutableNoEntryTiles() {
        return List.copyOf(noEntryTiles);
    }

    public Map<PawnColour, Integer> getStudentCount() { //the map will contain the colours of the placed pawns with relative counter
        Map<PawnColour, Integer> studentCount = new EnumMap<>(PawnColour.class);
        for (PawnColour p : this.getStudents()) {
            studentCount.merge(p, 1, Integer::sum);
        }
        return Map.copyOf(studentCount); // make sure the returned map is immutable
    }

    public List<PawnColour> getStudents() {
        List<PawnColour> islandGroupStudents = new ArrayList<>();
        for (Island s : this.getMutableIslands()) {
            islandGroupStudents.addAll(s.getStudents());
        }
        return List.copyOf(islandGroupStudents);
    }

    public boolean contains(Island i) {
        for (Island island : islands) {
            if (island.equals(i)) {
                return true;
            }
        }
        return false;
    }

    public void addNoEntry(NoEntryTile tile) {
        this.noEntryTiles.add(tile);
    }

    public void resetNoEntry() {
        this.noEntryTiles.remove(0).goHome();
    }

    public void swapTower(TowerStorage ts) {
        if (ts.getTowerCount() >= this.islands.size()) {
            for (Island i : this.islands) {
                i.swapTower(ts.extractTower());
            }
        }
    }

    @Override
    public String toString() {
        return "IslandGroup{" +
                "id=" + id +
                ", islands=" + islands +
                "noEntryTiles" + noEntryTiles +
                '}';
    }
}
