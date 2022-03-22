package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class Island implements Serializable {
    private final int id;
    private final ArrayList<PawnColour> students;
    private final boolean isLocked;
    private Optional<TowerColour> tower;

    public Island(int id) {
        this.id = id;
        this.students = new ArrayList<>();
        this.tower = Optional.empty();
        this.isLocked = false;
    }

    public int getId() {
        return id;
    }

    public ArrayList<PawnColour> getStudents() {
        return new ArrayList<>(students);
    }

    public Optional<TowerColour> getTower() {
        return tower;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void addStudent(StudentBag bag) {
        students.add(bag.extract());
    }

    public Optional<TowerColour> swapTower(TowerColour colour) {
        Optional<TowerColour> oldTowerColour = this.tower;
        this.tower = Optional.of(colour);
        return oldTowerColour;
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
