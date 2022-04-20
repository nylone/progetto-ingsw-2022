package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.toremove.NoPawnInCloudException;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

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

    public ArrayList<PawnColour> extractContents() throws NoPawnInCloudException {
        if (contents.size() > 0) {
            ArrayList<PawnColour> toReturn = new ArrayList<>(this.contents);
            this.contents = new ArrayList<>(3);
            return toReturn;
        } else {
            throw new NoPawnInCloudException();
        }
    }

    public ArrayList<PawnColour> getContents(){
        return new ArrayList<>(contents);
    }

    public void fill(ArrayList<PawnColour> colours) throws NoPawnInCloudException {
        if (contents.isEmpty()) {
            contents.addAll(colours); }
        else {
            throw new NoPawnInCloudException();
        }
    }
}

