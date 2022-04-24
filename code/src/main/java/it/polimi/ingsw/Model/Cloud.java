package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import static it.polimi.ingsw.Constants.CONTAINER_NAME_CLOUD;

public class Cloud implements Serializable {
    @Serial
    private static final long serialVersionUID = 118L; // convention: 1 for model, (01 -> 99) for objects

    private final int id;
    private ArrayList<PawnColour> contents;

    public Cloud(int id) {
        this.id = id;
        contents = new ArrayList<>(3);
    }

    public int getId() {
        return id;
    }

    public ArrayList<PawnColour> extractContents() throws EmptyContainerException {
        if (contents.size() > 0) {
            ArrayList<PawnColour> toReturn = new ArrayList<>(this.contents);
            this.contents = new ArrayList<>(3);
            return toReturn;
        } else {
            throw new EmptyContainerException(CONTAINER_NAME_CLOUD);
        }
    }

    public ArrayList<PawnColour> getContents() {
        return new ArrayList<>(contents);
    }

    public void fill(ArrayList<PawnColour> colours) throws FullContainerException {
        if (contents.isEmpty()) {
            contents.addAll(colours);
        } else {
            throw new FullContainerException(CONTAINER_NAME_CLOUD);
        }
    }
}

