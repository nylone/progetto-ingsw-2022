package it.polimi.ingsw.Controller.Enums;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

public class MoveDestination implements Serializable {
    @Serial
    private static final long serialVersionUID = 203L; // convention: 2 for controller, (01 -> 99) for objects
    private final DestinationType destinationType;
    private final Integer islandID;

    private MoveDestination(@NotNull Integer islandID) {
        this.destinationType = DestinationType.ISLAND;
        this.islandID = islandID;
    }

    private MoveDestination() {
        this.destinationType = DestinationType.DININGROOM;
        this.islandID = null;
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

    public int getIslandID() throws Exception {
        if (this.destinationType == DestinationType.ISLAND) {
            return this.islandID;
        } else {
            throw new Exception("DestinationType not compatible with request");
        }
    }
}