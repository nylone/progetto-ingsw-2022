package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.NoPawnInCloudException;

import java.util.ArrayList;

public class Cloud {
    private final int id;
    private final ArrayList<PawnColour> contents;

    public Cloud(int id) {
        this.id = id;
        contents = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public ArrayList<PawnColour> empty() throws NoPawnInCloudException {
        if (contents.size() > 0) {
            return new ArrayList<>(contents);
        } else {
            throw new NoPawnInCloudException(); //todo cosa fare se la nuvola è effettivamente vuota?
        }
    }

    public void fill(ArrayList<PawnColour> colours) {
        contents.addAll(colours);
    }
}

