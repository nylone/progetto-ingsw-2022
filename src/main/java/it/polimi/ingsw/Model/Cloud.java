package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static it.polimi.ingsw.Constants.CONTAINER_NAME_CLOUD;

/**
 * The Cloud class is used to replenish pawns in the entrance fields of the {@link PlayerBoard#getEntranceStudents()}
 */
public class Cloud implements Serializable {
    @Serial
    private static final long serialVersionUID = 118L; // convention: 1 for model, (01 -> 99) for objects

    private final int id;
    private ArrayList<PawnColour> contents;

    /**
     * Construct a Cloud, assigning an ID to it.
     * @param id if the cloud is in an array, the id will be the index of the array at which the cloud is located
     */
    public Cloud(int id) {
        this.id = id;
        contents = new ArrayList<>(3);
    }

    /**
     * The ID of the cloud is the index of the array in which the Cloud is stored
     * @return the ID of the cloud
     */
    public int getId() {
        return id;
    }

    /**
     * A Cloud will need to be emptied out, eventually. This method returns a {@link List} of {@link PawnColour} and empties the Cloud.
     * @return a {@link List} of {@link PawnColour}s that were contained in the Cloud
     * @throws EmptyContainerException if the Cloud being emptied was already empty, this exception is thrown.
     */
    public List<PawnColour> extractContents() throws EmptyContainerException {
        if (contents.size() > 0) {
            ArrayList<PawnColour> toReturn = new ArrayList<>(this.contents);
            this.contents = new ArrayList<>(3);
            return toReturn;
        } else {
            throw new EmptyContainerException(CONTAINER_NAME_CLOUD);
        }
    }

    /**
     * Cloud contents may be inspected without emptying the tile.
     * @return an {@link java.util.List#copyOf(Collection)} of the contents of the Cloud
     */
    public List<PawnColour> getContents() {
        return List.copyOf(contents);
    }

    /**
     * A cloud may need to be filled from time to time. this method is used for that
     * @param colours a {@link List} of {@link PawnColour}s to put in the Cloud tile
     * @throws FullContainerException if the Cloud being filled already had some contents, this exception will be thrown
     */
    public void fill(List<PawnColour> colours) throws FullContainerException {
        if (contents.isEmpty()) {
            contents.addAll(colours);
        } else {
            throw new FullContainerException(CONTAINER_NAME_CLOUD);
        }
    }
}

