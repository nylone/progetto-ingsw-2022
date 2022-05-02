package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enums.DestinationType;
import org.jetbrains.annotations.NotNull;

public class MoveDestination {
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