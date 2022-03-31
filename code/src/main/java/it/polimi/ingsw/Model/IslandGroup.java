package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;

import javax.swing.text.html.Option;
import java.io.Serializable;
import java.util.*;

public class IslandGroup implements Serializable {
    private final int id;
    private final ArrayList<Island> islands;
    private NoEntryTile noEntry;
    private boolean denyTowerInfluence;

    public IslandGroup(Island i) {
        this.islands = new ArrayList<>();
        this.id = i.getId();
        this.islands.add(i);
        this.denyTowerInfluence = false;
    }

    public IslandGroup(IslandGroup... islandGroups) {
        if (isJoinable(islandGroups)) {
            this.islands = new ArrayList<>();
            for (IslandGroup i : islandGroups) {
                islands.addAll(i.getIslands());
            }
            this.id = Arrays.stream(islandGroups)
                    .min(Comparator.comparingInt(islandGroup -> islandGroup.getId()))
                    .orElseThrow(() -> new InvalidInputException())
                    .getId();
        } else {
            throw new RuntimeException(); // todo implement unjoinablegroups exception
        }
    }

    public static boolean isJoinable(IslandGroup... groups) {
        return Arrays.stream(groups).allMatch(g -> g.equals(groups[0]));
    }

    public boolean canJoin(IslandGroup... groups) {
        return Arrays.stream(groups).allMatch(g -> g.equals(this));
    }

    public Optional<TowerColour> getTowerColour() {
        return this.getIslands().get(0).getTowerColour();
    }

    public int getTowerCount() {
        if (getTowerColour().isPresent()) return getIslands().size();
        else return 0;
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
    public Optional<IslandGroup> find(Island i){
        for(Island island : islands){
            if(island.getId()==i.getId()){
                return Optional.of(this);
            }
        }
        return Optional.empty();
    }

    public void setDenyTowerInfluence(boolean b){
        this.denyTowerInfluence = b;
    }
    public void setNoEntry(NoEntryTile t){
        this.noEntry = t;
    }

    public void resetNoEntry(){
        this.noEntry = null;
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
