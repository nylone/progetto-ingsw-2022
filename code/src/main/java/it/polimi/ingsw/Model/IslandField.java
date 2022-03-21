package it.polimi.ingsw.Model;

import it.polimi.ingsw.Misc.Utils;

import java.util.ArrayList;

public class IslandField {
    private static final int FIELD_SIZE = 12;
    private final ArrayList<IslandGroup> groups;
    private final ArrayList<Island> islands;
    private IslandGroup motherNaturePosition;

    public IslandField(GameBoard context) {
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

        for (Island island : this.islands) {
            int bagSize = context.getStudentBag().size();
            if (island.getId() != motherNatureIslandId && island.getId() != (motherNatureIslandId + 6) % FIELD_SIZE) {
                island.addStudent(context.getStudentBag().remove(bagSize - 1));
            }
        }
    }

    public ArrayList<IslandGroup> getGroups() {
        return new ArrayList<>(this.groups);
    }

    public ArrayList<Island> getIslands() {
        return new ArrayList<>(this.islands);
    }

    public IslandGroup getMotherNaturePosition() {
        return motherNaturePosition;
    }

    public void moveMotherNature(int moves) {
        motherNaturePosition = groups.get((groups.indexOf(motherNaturePosition) + moves) % groups.size());
    }

    private IslandGroup prevGroup(IslandGroup curr) {
        int groupSize = this.groups.size();
        int currIndex = this.groups.indexOf(curr);
        return groups.get(
                (currIndex + groupSize - 1) % groupSize
        );
    }

    private IslandGroup nextGroup(IslandGroup curr) {
        int groupSize = this.groups.size();
        return groups.get(
                (curr.getId() + groupSize + 1) % groupSize
        );
    }

    public void joinGroups() {
        IslandGroup motherGroup = this.getMotherNaturePosition();
        IslandGroup nextGroup = this.nextGroup(motherGroup);
        IslandGroup prevGroup = this.prevGroup(motherGroup);
        // look to the group before mother nature position and join if necessary
        if (motherGroup.getTower()
                .equals(
                        prevGroup.getTower()
                )) {
            IslandGroup joined = new IslandGroup(motherGroup, prevGroup);
            this.groups.remove(prevGroup);
            this.groups.set(this.groups.indexOf(motherGroup), joined);
        }
        if (motherGroup.getTower()
                .equals(
                        nextGroup.getTower()
                )) {
            IslandGroup joined = new IslandGroup(motherGroup, nextGroup);
            this.groups.remove(nextGroup);
            this.groups.set(this.groups.indexOf(motherGroup), joined);
        }
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
