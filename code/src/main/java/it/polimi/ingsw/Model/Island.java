package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class Island implements Serializable {
    private final int id;
    private final ArrayList<PawnColour> students;
    private final boolean isLocked;
    private Tower tower;

    public Island(int id) {
        this.id = id;
        this.students = new ArrayList<>();
        this.tower = null;
        this.isLocked = false;
    }

    public int getId() {
        return id;
    }

    public ArrayList<PawnColour> getStudents() {
        return new ArrayList<>(students);
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void addStudent(StudentBag bag) {
        students.add(bag.extract());
    }

    public void addStudent(PawnColour p){students.add(p);}

    public void swapTower(Tower t) {
        if (this.tower != null) this.tower.linkBackToStorage();
        this.tower = t;
    }

    public Optional<TowerColour> getTowerColour() {
        if (this.tower == null) return Optional.empty();
        else return Optional.of(this.tower.getColour());
    }

    //test-purpose only

    @Override
    public String toString() {
        return "Island{" +
                "id=" + id +
                ", students=" + students +
                ", tower=" + tower +
                ", isLocked=" + isLocked +
                '}';
    }
}
