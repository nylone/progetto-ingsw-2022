package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.TowerColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Island implements Serializable {
    @Serial
    private static final long serialVersionUID = 121L; // convention: 1 for model, (01 -> 99) for objects

    private final int id;
    private final ArrayList<PawnColour> students;
    private Tower tower;

    public Island(int id) {
        this.id = id;
        this.students = new ArrayList<>();
        this.tower = null;
    }

    public int getId() {
        return id;
    }

    public ArrayList<PawnColour> getStudents() {
        return new ArrayList<>(students);
    }

    public Optional<TowerColour> getTowerColour() {
        if (this.tower == null) return Optional.empty();
        else return Optional.of(this.tower.getColour());
    }

    public void addStudent(StudentBag bag) {
        try {
            students.add(bag.extract());
        } catch (EmptyContainerException e) {
            // should never happen
            Logger.severe("student bag was found empty while adding a student to an island. Critical, unrecoverable, error");
            throw new RuntimeException(e);
        }
    }

    public void addStudent(PawnColour p) {
        students.add(p);
    }

    public void swapTower(Tower t) {
        if (this.tower != null) this.tower.linkBackToStorage();
        this.tower = t;
    }

    //test-purpose only

    @Override
    public String toString() {
        return "Island{" +
                "id=" + id +
                ", students=" + students +
                ", tower=" + tower +
                '}';
    }
}
