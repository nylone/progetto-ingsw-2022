package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StudentBag implements Serializable {
    @Serial
    private static final long serialVersionUID = 130L; // convention: 1 for model, (01 -> 99) for objects
    private final ArrayList<PawnColour> studentBag;

    public StudentBag(int numOfStudentsPerColour) {
        this.studentBag = new ArrayList<>(numOfStudentsPerColour * PawnColour.values().length);
        for (PawnColour colour :
                PawnColour.values()) {
            for (int i = 0; i < numOfStudentsPerColour; i++) {
                studentBag.add(colour);
            }
        }
        Collections.shuffle(this.studentBag, new Random(System.currentTimeMillis()));
    }

    public PawnColour extract() {
        return this.studentBag.remove(this.studentBag.size() - 1);
    }

    public List<PawnColour> multiple_extraction(int extractions){
        List<PawnColour> extracted = new ArrayList<>();
        for(int i=0; i<extractions; i++) extracted.add(this.extract());
        return extracted;
    }

    public void appendAndShuffle(PawnColour colour) {
        this.studentBag.add(colour);
        Collections.shuffle(this.studentBag, new Random(System.currentTimeMillis()));
    }
}
