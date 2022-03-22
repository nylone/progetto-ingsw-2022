package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class StudentBag implements Serializable {
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
        return this.studentBag.remove(this.studentBag.size() -1);
    }

    public void appendAndShuffle(PawnColour colour) {
        this.studentBag.add(colour);
        Collections.shuffle(this.studentBag, new Random(System.currentTimeMillis()));
    }
}
