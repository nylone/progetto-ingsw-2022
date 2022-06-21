package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Misc.Optional;

import java.io.Serial;
import java.io.Serializable;

public class MoveDestination implements Serializable {
    @Serial
    private static final long serialVersionUID = 203L; // convention: 2 for controller, (01 -> 99) for objects
    private final DestinationType destinationType;
    private final Optional<Integer> islandID;

    private MoveDestination(int islandID) {
        this.destinationType = DestinationType.ISLAND;
        this.islandID = Optional.of(islandID);
    }

    private MoveDestination() {
        this.destinationType = DestinationType.DININGROOM;
        this.islandID = Optional.empty();
    }

    public static MoveDestination toDiningRoom() {
        return new MoveDestination();
    }

    public static MoveDestination toIsland(int islandID) {
        return new MoveDestination(islandID);
    }

    public DestinationType getDestinationType() {
        return destinationType;
    }

    public Integer getIslandID() {
        if (this.destinationType == DestinationType.ISLAND && this.islandID.isPresent()) {
            return this.islandID.get();
        } else {
            return null;
        }
    }
}