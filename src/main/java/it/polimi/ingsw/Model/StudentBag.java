package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The StudentBag contains a limited number of {@link PawnColour} ordered randomly.
 */
public class StudentBag implements Serializable {
    @Serial
    private static final long serialVersionUID = 130L; // convention: 1 for model, (01 -> 99) for objects
    private ArrayList<PawnColour> studentBag;
    private boolean isEmpty;

    /**
     * Construct the bag
     * @param numOfStudentsPerColour the number of each color of students to put in the bag
     */
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

    /**
     * Check to see if the bag is empty
     * @return true if the bag is empty
     */
    public boolean isEmpty() {
        return this.isEmpty;
    }

    /**
     * Extract multiple {@link PawnColour}s at once
     * @param extractions number of maximum extractions to carry out. The number of extracted students may be lower than the
     *                    number specified as input, if the bag empties out during the extraction
     * @return an Unmodifiable {@link List} containing the extracted {@link PawnColour}
     */
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
        return List.copyOf(extracted);
    }

    /**
     * Extract a single {@link PawnColour} from the bag
     * @return the extracted student
     * @throws EmptyContainerException if the bag is empty
     */
    public PawnColour extract() throws EmptyContainerException {
        if (this.isEmpty) throw new EmptyContainerException("StudentBag");
        if (this.getSize() == 1) this.isEmpty = true;
        return this.studentBag.remove(this.studentBag.size() - 1);
    }

    /**
     * Check the remaining students in the bag
     * @return the size of the bag
     */
    public int getSize() {
        return studentBag.size();
    }

    /**
     * Put a student back in the bag and shuffle it in.
     * @param colour the {@link PawnColour} to add back to the bag
     */
    public void appendAndShuffle(PawnColour colour) {
        if (this.getSize() == 0) this.isEmpty = false;
        this.studentBag.add(colour);
        Utils.shuffle(this.studentBag);
    }

    /**
     * Used to sanitize {@link Model}. Removes information about the internal contents of the bag, making the spoofing of
     * technically hidden information impossible on the client side.
     */
    public void removeContentReference() {
        this.studentBag = null;
    }
}
