package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class IslandGroup implements Serializable {
    private final int id;
    private final ArrayList<Island> islands;

    public IslandGroup(Island i) {
        this.islands = new ArrayList<Island>();
        this.id = i.getId();
        this.islands.add(i);
    }

    public IslandGroup(IslandGroup... islandGroups) {
        if (joinable(islandGroups)) {
            this.islands = new ArrayList<>();
            for (IslandGroup i : islandGroups) {
                islands.addAll(i.getIslands());
            }
            this.id = Arrays.stream(islandGroups)
                    .min((first, second) -> Integer.compare(first.id, second.id))
                    .orElseThrow()
                    .getId();
        } else {
            throw new RuntimeException(); // todo implement unjoinablegroups exception
        }
    }

    public static boolean joinable(IslandGroup... groups) {
        return Arrays.stream(groups).allMatch(g -> g.equals(groups[0]));
    }

    public boolean canJoin(IslandGroup... groups) {
        return Arrays.stream(groups).allMatch(g -> g.equals(this));
    }

    public TowerColour getTowerColour() {
        return this.getIslands().get(0).getTowerColour();
    }

    public int getId() {
        return id;
    }

    public ArrayList<Island> getIslands() {
        return new ArrayList<>(islands);
    }

    public Map<PawnColour, Integer> getStudentCount() { //the map will contain the colours of the placed pawns with relative counter
        Map<PawnColour, Integer> studentCount = new EnumMap<>(PawnColour.class);
        for (PawnColour p : this.getStudents()) {
            studentCount.merge(p, 1, Integer::sum);
        }
        return studentCount;
    }

    public ArrayList<PawnColour> getStudents() {
        ArrayList<PawnColour> islandGroupStudents = new ArrayList<>();
        for (Island s : this.getIslands()) {
            islandGroupStudents.addAll(s.getStudents());
        }
        return islandGroupStudents;
    }

    public void swapTower(TowerStorage ts) {
        if (ts.getTowerCount() >= this.islands.size()) {
            for (Island i : this.islands) {
                i.swapTower(ts.extractTower());
            }
        }
    }

    //test-purpose only
    @Override
    public String toString() {
        return "ISLANDGROUP{" +
                "id=" + id +
                ", islands=" + islands +
                '}';
    }
}
