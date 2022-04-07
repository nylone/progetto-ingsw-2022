package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;
import it.polimi.ingsw.Exceptions.NoParamaterException;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class IslandGroup implements Serializable {
    @Serial
    private static final long serialVersionUID = 123L; // convention: 1 for model, (01 -> 99) for objects

    private final int id;
    private final ArrayList<Island> islands;
    private Optional<NoEntryTile> noEntry;
    private boolean denyTowerInfluence;

    public IslandGroup(Island i) {
        this.islands = new ArrayList<>();
        this.id = i.getId();
        this.islands.add(i);
        this.denyTowerInfluence = false;
    }

    public IslandGroup(IslandGroup... islandGroups) {
        if (islandGroups.length > 0) {
            if (islandGroups[0].isJoinable(islandGroups)) {
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
        }else{
            try {
                throw new NoParamaterException();
            } catch (NoParamaterException e) {
                e.printStackTrace();
                this.id=0;
                this.islands = null;

            }
        }
    }

    // returns true if the inputted IslandGroups all contain the same type of tower
    public boolean isJoinable(IslandGroup... groups) {
        if (groups.length <= 0){
                return false;
        }else {
            return Arrays.stream(groups).allMatch(g -> g.getTowerColour().equals(this.getTowerColour()));
        }
    }

    public Optional<TowerColour> getTowerColour() {
        return this.getIslands().get(0).getTowerColour();
    }

    public int getTowerCount() {
        if (getTowerColour().isPresent()) return getIslands().size();
        else return 0;
    }

    public boolean getDenyTowerInfluence(){
        return denyTowerInfluence;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Island> getIslands() {
        return new ArrayList<>(islands);
    }

    public Optional<NoEntryTile> getNoEntry() {
        return noEntry;
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
    public void setNoEntry(Optional<NoEntryTile> t){
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
