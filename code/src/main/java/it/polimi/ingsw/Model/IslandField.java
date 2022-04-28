package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Operation.OperationException;
import it.polimi.ingsw.Misc.Utils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Constants.CONTAINER_NAME_ISLANDFIELD_ISLANDGROUPS;
import static it.polimi.ingsw.Constants.CONTAINER_NAME_ISLANDFIELD_ISLANDS;

public class IslandField implements Serializable {
    @Serial
    private static final long serialVersionUID = 122L; // convention: 1 for model, (01 -> 99) for objects

    private static final int FIELD_SIZE = 12;
    private final ArrayList<IslandGroup> groups;
    private final ArrayList<Island> islands;
    private IslandGroup motherNaturePosition;

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
                island.addStudent(tempBag);
            }
        }
    }

    // GETTER METHODS
    public List<IslandGroup> getMutableGroups() {
        return List.copyOf(this.groups);
    }

    public List<Island> getMutableIslands() {
        return List.copyOf(this.islands);
    }

    public IslandGroup getMutableIslandGroupById(int id) throws InvalidContainerIndexException {
        return groups.stream()
                .filter(g -> g.getId() == id)
                .findAny()
                .orElseThrow(() -> new InvalidContainerIndexException(CONTAINER_NAME_ISLANDFIELD_ISLANDGROUPS));
    }

    public Island getMutableIslandById(int id) throws InvalidContainerIndexException {
        return islands.stream()
                .filter(i -> i.getId() == id)
                .findAny()
                .orElseThrow(() -> new InvalidContainerIndexException(CONTAINER_NAME_ISLANDFIELD_ISLANDS));
    }

    protected void moveMotherNature(int moves) {
        motherNaturePosition = groups.get((groups.indexOf(motherNaturePosition) + moves) % groups.size());
        System.out.println(motherNaturePosition);
    }

    public boolean joinGroups() {
        boolean didJoin = false;
        IslandGroup motherGroup = this.getMutableMotherNaturePosition();
        IslandGroup nextGroup = this.nextGroup(motherGroup);
        IslandGroup prevGroup = this.prevGroup(motherGroup);
        // look to the group before mother nature position and join if necessary
        try {
            if (motherGroup.isJoinable(prevGroup)) {
                IslandGroup joined = new IslandGroup(motherGroup, prevGroup);
                this.groups.remove(prevGroup);
                this.groups.set(this.groups.indexOf(motherGroup), joined);
                this.motherNaturePosition = joined;
                didJoin = true;
            }
            motherGroup = this.getMutableMotherNaturePosition();
            if (motherGroup.isJoinable(nextGroup)) {
                IslandGroup joined = new IslandGroup(motherGroup, nextGroup);
                this.groups.remove(nextGroup);
                this.groups.set(this.groups.indexOf(motherGroup), joined);
                this.motherNaturePosition = joined;
                didJoin = true;
            }
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return didJoin;
    }

    public IslandGroup getMutableMotherNaturePosition() {
        return motherNaturePosition;
    }

    private IslandGroup nextGroup(IslandGroup curr) {
        int groupSize = this.groups.size();
        return groups.get(
                (curr.getId() + groupSize + 1) % groupSize
        );
    }

    private IslandGroup prevGroup(IslandGroup curr) {
        int groupSize = this.groups.size();
        int currIndex = this.groups.indexOf(curr);
        return groups.get(
                (currIndex + groupSize - 1) % groupSize
        );
    }

    //test-purpose only
    @Override
    public String toString() {
        return "IslandField{" +
                ", groups=" + groups +
                ", islands=" + islands +
                ", motherNaturePosition=" + motherNaturePosition +
                '}';
    }
}
