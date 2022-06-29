package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Operation.FailedOperationException;
import it.polimi.ingsw.Exceptions.Operation.ForbiddenOperationException;
import it.polimi.ingsw.Exceptions.Operation.OperationException;
import it.polimi.ingsw.Misc.SerializableOptional;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.TowerColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Every {@link Island} can be contained in an IslandGroup and multiple Islands can be grouped up.
 */
public class IslandGroup implements Serializable {
    @Serial
    private static final long serialVersionUID = 123L; // convention: 1 for model, (01 -> 99) for objects

    private final int id;
    private final ArrayList<Island> islands;
    private final Stack<NoEntryTile> noEntryTiles;

    /**
     * Construct an IslandGroup starting from a single {@link Island}
     *
     * @param i a single {@link Island}, its {@link Island#getId()} becomes the group's ID
     */
    public IslandGroup(Island i) {
        this.islands = new ArrayList<>();
        this.id = i.getId();
        this.islands.add(i);
        this.noEntryTiles = new Stack<>();
    }

    /**
     * Construct a new amalgamation of groups. The new group contains the sum of all the {@link PawnColour} on each group, the sum of the
     * {@link NoEntryTile}s on each group. Groups can only be joined if their {@link Tower#getColour()} returns the same value.<br>
     * The ID assigned to the new group will be the lowest between the input groups.
     *
     * @param islandGroups an array of groups to be merged into one
     * @throws OperationException if the groups cannot be joined
     */
    public IslandGroup(IslandGroup... islandGroups) throws OperationException {
        if (islandGroups.length > 0 && islandGroups[0].canJoin(islandGroups)) {
            this.islands = new ArrayList<>();
            this.noEntryTiles = new Stack<>();
            for (IslandGroup i : islandGroups) {
                this.islands.addAll(i.getMutableIslands());
                this.noEntryTiles.addAll(i.getMutableNoEntryTiles());
            }
            this.id = Arrays.stream(islandGroups)
                    .min(Comparator.comparingInt(IslandGroup::getId))
                    .orElseThrow(() -> new FailedOperationException("Island Groups Constructor"))
                    .getId();
        } else {
            throw new ForbiddenOperationException("Island Groups Constructor");
        }
    }

    /**
     * returns true if the inputted {@link IslandGroup} all contain the same type of tower
     *
     * @param groups the groups you'd like to join
     */
    public boolean canJoin(IslandGroup... groups) {
        if (groups.length <= 0) {
            return false;
        } else {
            if (this.getTowerColour().isEmpty()) return false;
            else return
                    Arrays.stream(groups).allMatch(g -> g.getTowerColour().equals(this.getTowerColour()));
        }
    }

    /**
     * @return an unmodifiable {@link List} containing all the {@link Island}s in the group. Each element on the list
     * CAN be modified, the list itself can't.
     */
    public List<Island> getMutableIslands() {
        return List.copyOf(islands);
    }

    /**
     * @return an unmodifiable {@link List} containing all the {@link NoEntryTile}s in the group. Each element on the list
     * CAN be modified, the list itself can't.
     */
    public List<NoEntryTile> getMutableNoEntryTiles() {
        return List.copyOf(noEntryTiles);
    }

    /**
     * @return the ID of the group
     */
    public int getId() {
        return id;
    }

    /**
     * Get the colour of the towers stored on the islands.
     *
     * @return a non empty {@link SerializableOptional} containing the {@link TowerColour}, if present. Note: if at least an island doesn't match the {@link TowerColour} of the others, this method will return an empty
     * {@link SerializableOptional}.
     */
    public SerializableOptional<TowerColour> getTowerColour() {
        List<Island> islands = this.getMutableIslands();
        if (islands.stream().allMatch(i -> i.getTowerColour().equals(islands.get(0).getTowerColour()))) {
            return islands.get(0).getTowerColour();
        } else return SerializableOptional.empty();
    }

    /**
     * @return the amount of towers in the group (either 0 or equal to the number of islands in the group)
     */
    public int getTowerCount() {
        if (getTowerColour().isPresent()) return getMutableIslands().size();
        else return 0;
    }

    /**
     * @return an unmodifiable {@link Map} a {@link PawnColour} to the amount of students of that colour in the group.
     */
    public Map<PawnColour, Integer> getStudentCount() {
        Map<PawnColour, Integer> studentCount = new EnumMap<>(PawnColour.class);
        for (PawnColour p : this.getStudents()) {
            studentCount.merge(p, 1, Integer::sum);
        }
        return Map.copyOf(studentCount);
    }

    /**
     * @return an unmodifiable {@link List} containing all the {@link PawnColour}s in the group.
     */
    public List<PawnColour> getStudents() {
        List<PawnColour> islandGroupStudents = new ArrayList<>();
        for (Island s : this.getMutableIslands()) {
            islandGroupStudents.addAll(s.getStudents());
        }
        return List.copyOf(islandGroupStudents);
    }

    /**
     * Checks to see if an island is contained in the group
     *
     * @param i the {@link Island} you wish to search for in the group
     * @return true if the island is contained, false otherwise
     */
    public boolean contains(Island i) {
        for (Island island : islands) {
            if (island.equals(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * adds a {@link NoEntryTile} to the group
     *
     * @param tile the no entry tile to add
     */
    public void addNoEntry(NoEntryTile tile) {
        this.noEntryTiles.add(tile);
    }

    /**
     * removes a {@link NoEntryTile} from the stack of tiles and puts it back on the character card where it came from
     */
    public void resetNoEntry() {
        this.noEntryTiles.remove(0).goHome();
    }

    /**
     * multiple {@link Tower}s may need to be swapped or added during the Group's lifespan, this method can be used for that
     *
     * @param ts the new {@link TowerStorage} where towers are coming from. the old towers (if any were present) will all be returned to its
     *           rightful storage automatically. If the new tower storage runs out of towers before swapping out all the towers from the group,
     *           then some islands fill be left empty while others will be full. Note that the empty islands will keep staying in
     *           the group, which is going to become un-join able.
     */
    public void swapTower(TowerStorage ts) {
        for (Island i : this.islands) {
            i.swapTower(ts.extractTower());
        }
    }
    /*
    @Override
    public String toString() {
        return "IslandGroup{" +
                "id=" + id +
                ", islands=" + islands +
                "noEntryTiles" + noEntryTiles +
                '}';
    }
    //*/
}
