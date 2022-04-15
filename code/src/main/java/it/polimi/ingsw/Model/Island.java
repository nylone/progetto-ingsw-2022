package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.TowerColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class Island implements Serializable {
    @Serial
    private static final long serialVersionUID = 121L; // convention: 1 for model, (01 -> 99) for objects

    private final int id;
    private final ArrayList<PawnColour> students;
    private Tower tower;
    private boolean isLocked;

    public Island(int id) {
        this.id = id;
        this.students = new ArrayList<>();
        this.tower = null;
        this.isLocked = false;
    }

    public int getId() {
        return id;
    }

    public boolean getIsLocked() {
        return this.isLocked;
    }


    public ArrayList<PawnColour> getStudents() {
        return new ArrayList<>(students);
    }

    public Optional<TowerColour> getTowerColour() {
        if (this.tower == null) return Optional.empty();
        else return Optional.of(this.tower.getColour());
    }

    public void setIsLocked(boolean locked) {
        this.isLocked = locked;
    }
    public void addStudent(StudentBag bag) {
        students.add(bag.extract());
    }

    public void addStudent(PawnColour p){students.add(p);}

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
                "isLocked" + isLocked +
                '}';
    }
}
