package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.MoveStudent;
import it.polimi.ingsw.Misc.OptionalValue;

import java.io.Serial;
import java.io.Serializable;

/**
 * This class is used by {@link MoveStudent} to direct a pawn from the entrance to the correct destination
 */
public class MoveDestination implements Serializable {
    @Serial
    private static final long serialVersionUID = 203L; // convention: 2 for controller, (01 -> 99) for objects
    private final DestinationType destinationType;
    private final OptionalValue<Integer> islandID;

    /**
     * Constructor used to point to an island
     *
     * @param islandID the id of the island to point to
     */
    private MoveDestination(int islandID) {
        this.destinationType = DestinationType.ISLAND;
        this.islandID = OptionalValue.of(islandID);
    }

    /**
     * Constructor used to point to the dining room
     */
    private MoveDestination() {
        this.destinationType = DestinationType.DININGROOM;
        this.islandID = OptionalValue.empty();
    }

    /**
     * call this method if you wish to point to the dining room
     *
     * @return a new object pointing to the dining room
     */
    public static MoveDestination toDiningRoom() {
        return new MoveDestination();
    }

    /**
     * call this method if you wish to point to an island
     *
     * @param islandID the id of the island to point to
     * @return a new object pointing to the desired island id
     */
    public static MoveDestination toIsland(int islandID) {
        return new MoveDestination(islandID);
    }

    /**
     * @return the {@link DestinationType} selected during creation of the object
     */
    public DestinationType getDestinationType() {
        return destinationType;
    }

    /**
     * @return the ID of the island this class is pointing to, or null if the {@link DestinationType} is {@link DestinationType#DININGROOM}
     */
    public Integer getIslandID() {
        if (this.destinationType == DestinationType.ISLAND && this.islandID.isPresent()) {
            return this.islandID.get();
        } else {
            return null;
        }
    }
}