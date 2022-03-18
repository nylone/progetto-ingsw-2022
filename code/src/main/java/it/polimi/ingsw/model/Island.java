package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Optional;

public class Island {
    private int id;
    private ArrayList<PawnColour> students;
    private Optional<TowerColour> tower;
    private boolean isLocked;

    public  Island(int id){
        this.id = id;
        students = new ArrayList<>();
        this.tower = Optional.empty();
        this.isLocked = false;
    }

    public int getId() {
        return id;
    }
    public ArrayList<PawnColour> getStudents(){
        return new ArrayList<>(students);
    }

    public Optional<TowerColour> getTower() {
        return tower;
    }
    public boolean isLocked(){
        return isLocked;
    }
    public Optional<TowerColour> swapTower(TowerColour colour){
        Optional<TowerColour> oldTowerColour = this.tower;
        this.tower = Optional.ofNullable(colour);
        return oldTowerColour;

    }
    public void placeStudent(PawnColour colour){
        students.add(colour);
    }
}
