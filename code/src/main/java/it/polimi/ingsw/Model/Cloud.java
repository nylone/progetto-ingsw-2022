package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.NoPawnInCloudException;

import java.util.ArrayList;

public class Cloud {
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

    public ArrayList<PawnColour> getContents() throws NoPawnInCloudException {
        return new ArrayList<>(contents);
    }

    public void fill(ArrayList<PawnColour> colours) {
        // todo check that the cloud is empty before filling it
        contents.addAll(colours);
    }
}
