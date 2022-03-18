package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IslandGroup {
    private int id;
    private ArrayList<Island> islands;

    public IslandGroup(Island i) {
        islands = new ArrayList<>();
        islands.add(i);
    }

    public IslandGroup(IslandGroup... islandGroup) {
        islands = new ArrayList<>();
        for (IslandGroup i : islandGroup) {
            islands.addAll(i.getIslands());
        }
    }

    public int getId() {
        return id;
    }

    public ArrayList<Island> getIslands() {
        return islands;
    }

    public Map<PawnColour, Integer> getStudentCount() { //the map will contain
        Map<PawnColour, Integer> studentCount = new HashMap<>();
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
}
