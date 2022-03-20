package it.polimi.ingsw.model;

import java.util.*;

public class IslandGroup {
    private int id;
    private ArrayList<Island> islands;

    public IslandGroup(Island i) {
        islands = new ArrayList<>();
        this.id = i.getId();
        islands.add(i);
    }

    public IslandGroup(IslandGroup... islandGroup) {
        islands = new ArrayList<>();
        for (IslandGroup i : islandGroup) {
            islands.addAll(i.getIslands());
        }
        this.id = Arrays.stream(islandGroup).min((first, second) -> Integer.compare(first.id, second.id)).get().id;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Island> getIslands() {
        return islands;
    }

    public Map<PawnColour, Integer> getStudentCount() { //the map will contain the colours of the placed pawns with relative counter
        Map<PawnColour, Integer> studentCount = new EnumMap<>(PawnColour.class);
        for (Island s : islands) {
            for (PawnColour p : s.getStudents()) {
                studentCount.merge(p, 1, Integer::sum);
            }
        }
        return studentCount;
    }

    public ArrayList<PawnColour> getStudents() {
        ArrayList<PawnColour> islandGroupStudents = new ArrayList<>();
        for (Island s : islands) {
            islandGroupStudents.addAll(s.getStudents());
        }
        return islandGroupStudents;
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
