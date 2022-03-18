package it.polimi.ingsw.model;
import it.polimi.ingsw.Exceptions.NoPawnInCloudException;

import java.util.ArrayList;
import java.util.Arrays;

public class Cloud {
    private int id;
    private ArrayList<PawnColour> contents;


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

