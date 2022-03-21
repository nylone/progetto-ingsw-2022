package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;

import java.util.ArrayList;
import java.util.Optional;

public class Island {
    private final int id;
    private final ArrayList<PawnColour> students;
    private Optional<TowerColour> tower;
    private final boolean isLocked;

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

    public void addStudent(PawnColour colour) {
            students.add(colour);
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
