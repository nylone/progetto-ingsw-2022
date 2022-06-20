package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StudentBag implements Serializable {
    @Serial
    private static final long serialVersionUID = 130L; // convention: 1 for model, (01 -> 99) for objects
    private ArrayList<PawnColour> studentBag;
    private boolean isEmpty;

    public StudentBag(int numOfStudentsPerColour) {
        this.studentBag = new ArrayList<>(numOfStudentsPerColour * PawnColour.values().length);
        for (PawnColour colour :
                PawnColour.values()) {
            for (int i = 0; i < numOfStudentsPerColour; i++) {
                studentBag.add(colour);
            }
        }
        Utils.shuffle(this.studentBag);
        this.isEmpty = false;
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public List<PawnColour> multipleExtraction(int extractions) {
        List<PawnColour> extracted = new ArrayList<>();
        for (int i = 0; i < extractions && !this.isEmpty; i++) {
            try {
                extracted.add(this.extract());
            } catch (EmptyContainerException e) {
                // this catch clause should never be executed
                throw new RuntimeException(e);
            }
        }
        return extracted;
    }

    public PawnColour extract() throws EmptyContainerException {
        if (this.isEmpty) throw new EmptyContainerException("StudentBag");
        if (this.getSize() == 1) this.isEmpty = true;
        return this.studentBag.remove(this.studentBag.size() - 1);
    }

    public int getSize() {
        return studentBag.size();
    }

    public void appendAndShuffle(PawnColour colour) {
        if (this.getSize() == 0) this.isEmpty = false;
        this.studentBag.add(colour);
        Utils.shuffle(this.studentBag);
    }

    public void removeContentReference() {
        this.studentBag = null;
    }
}
