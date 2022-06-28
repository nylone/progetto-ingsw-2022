package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Operation.OperationException;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Misc.Utils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Constants.CONTAINER_NAME_ISLANDFIELD_ISLANDGROUPS;
import static it.polimi.ingsw.Constants.CONTAINER_NAME_ISLANDFIELD_ISLANDS;

/**
 * All {@link IslandGroup}s in the game need to be contained in a circular structure. This class does just that - along
 * joining the groups and tracking Mother Nature's position
 */
public class IslandField implements Serializable {
    @Serial
    private static final long serialVersionUID = 122L; // convention: 1 for model, (01 -> 99) for objects

    private static final int FIELD_SIZE = 12;
    private final ArrayList<IslandGroup> groups;
    private final ArrayList<Island> islands;
    private IslandGroup motherNaturePosition;

    /**
     * Creates the Field, populating it with 12 {@link Island}s and 12 {@link IslandGroup}s.
     * The generated elements are populated with following the rules of the game.
     */
    public IslandField() {
        this.groups = new ArrayList<>(FIELD_SIZE);
        this.islands = new ArrayList<>(FIELD_SIZE);
        for (int i = 0; i < FIELD_SIZE; i++) {
            Island island = new Island(i);
            IslandGroup islandGroup = new IslandGroup(island);
            this.islands.add(island);
            this.groups.add(islandGroup);
        }

        this.motherNaturePosition = Utils.random(this.groups);
        int motherNatureIslandId = this.motherNaturePosition.getId();

        StudentBag tempBag = new StudentBag(2);

        for (Island island : this.islands) {
            if (island.getId() != motherNatureIslandId && island.getId() != (motherNatureIslandId + 6) % FIELD_SIZE) {
                try {
                    island.addStudent(tempBag.extract());
                } catch (EmptyContainerException e) {
                    // should never happen
                    Logger.severe("student bag was found empty while adding a student to an island. Critical, unrecoverable, error");
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * @return an unmodifiable {@link List} containing all the {@link IslandGroup}s on the field. Each element on the list
     * CAN be modified, the list itself can't.
     */
    public List<IslandGroup> getMutableGroups() {
        return List.copyOf(this.groups);
    }

    /**
     * @return an unmodifiable {@link List} containing all the {@link Island}s on the field. Each element on the list
     * CAN be modified, the list itself can't.
     */
    public List<Island> getMutableIslands() {
        return List.copyOf(this.islands);
    }

    /**
     * @param id the ID of the group to search for
     * @return the {@link IslandGroup} matching the ID on the input
     * @throws InvalidContainerIndexException if the specified ID is not in the list of currently active groups, this exception
     *                                        is thrown
     */
    public IslandGroup getMutableIslandGroupById(int id) throws InvalidContainerIndexException {
        return groups.stream()
                .filter(g -> g.getId() == id)
                .findAny()
                .orElseThrow(() -> new InvalidContainerIndexException(CONTAINER_NAME_ISLANDFIELD_ISLANDGROUPS));
    }

    /**
     * @param id the ID of the island to search for
     * @return the {@link Island} matching the ID on the input
     * @throws InvalidContainerIndexException if the specified ID is not in the list of currently active islands, this exception
     *                                        is thrown
     */
    public Island getMutableIslandById(int id) throws InvalidContainerIndexException {
        return islands.stream()
                .filter(i -> i.getId() == id)
                .findAny()
                .orElseThrow(() -> new InvalidContainerIndexException(CONTAINER_NAME_ISLANDFIELD_ISLANDS));
    }

    /**
     * this method is Package-private - moving mother nature through this method does not enact its power. it is therefore unsafe
     * to call this outside of very specific circumstances.
     *
     * @param moves the amount of moves mother nature will move.
     */
    protected void moveMotherNature(int moves) {
        motherNaturePosition = groups.get((groups.indexOf(motherNaturePosition) + moves) % groups.size());
    }

    /**
     * When called, this method will join the groups adjacent to mother nature following the game's rules.
     */
    public void joinGroups() {
        IslandGroup motherGroup = this.getMutableMotherNaturePosition();
        IslandGroup nextGroup = this.nextGroup(motherGroup);
        IslandGroup prevGroup = this.prevGroup(motherGroup);
        // look to the group before mother nature position and join if necessary
        ifJoinableThenJoin(motherGroup, prevGroup);
        motherGroup = this.getMutableMotherNaturePosition();
        ifJoinableThenJoin(motherGroup, nextGroup);
    }

    /**
     * @return the {@link IslandGroup} matching the position of mother nature
     */
    public IslandGroup getMutableMotherNaturePosition() {
        return motherNaturePosition;
    }

    /**
     * searches for the {@link IslandGroup} adjacent to a selected one
     *
     * @param curr the {@link IslandGroup} you wish to find the next adjacent one
     * @return the {@link IslandGroup} that comes after the one in input
     */
    private IslandGroup nextGroup(IslandGroup curr) {
        int groupSize = this.groups.size();
        int currIndex = this.groups.indexOf(curr);
        return groups.get(
                (currIndex + groupSize + 1) % groupSize
        );
    }

    /**
     * searches for the {@link IslandGroup} adjacent to a selected one
     *
     * @param curr the {@link IslandGroup} you wish to find the previous adjacent one
     * @return the {@link IslandGroup} that comes before the one in input
     */
    private IslandGroup prevGroup(IslandGroup curr) {
        int groupSize = this.groups.size();
        int currIndex = this.groups.indexOf(curr);
        return groups.get(
                (currIndex + groupSize - 1) % groupSize
        );
    }

    /**
     * Two groups are considered joinable if they share the same tower colour.
     *
     * @param motherGroup the group on which Mother nature is standing
     * @param otherGroup  a group adjacent to motherGroup
     */
    private void ifJoinableThenJoin(IslandGroup motherGroup, IslandGroup otherGroup) {
        try {
            if (motherGroup.canJoin(otherGroup)) {
                IslandGroup joined = new IslandGroup(motherGroup, otherGroup);
                this.groups.remove(otherGroup);
                this.groups.set(this.groups.indexOf(motherGroup), joined);
                this.motherNaturePosition = joined;
            }
        } catch (OperationException e) {
            Logger.severe("Unreachable statement has been reached. Severe, unrecoverable error");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /*//test-purpose only
    @Override
    public String toString() {
        return "IslandField{" +
                ", groups=" + groups +
                ", islands=" + islands +
                ", motherNaturePosition=" + motherNaturePosition +
                '}';
    }
    //*/
}
