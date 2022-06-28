package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Misc.SerializableOptional;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.TowerColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Islands are containers of {@link PawnColour}s and {@link Tower}.The islands can be grouped through {@link IslandGroup}
 */
public class Island implements Serializable {
    @Serial
    private static final long serialVersionUID = 121L; // convention: 1 for model, (01 -> 99) for objects

    private final int id;
    private final ArrayList<PawnColour> students;
    private Tower tower;

    /**
     * Construct an Island, assinging an ID to it.
     * @param id the id of the constructed Island.
     */
    public Island(int id) {
        this.id = id;
        this.students = new ArrayList<>();
        this.tower = null;
    }

    /**
     * @return the ID of the Island
     */
    public int getId() {
        return id;
    }

    /**
     * @return the {@link PawnColour}s contained in the Island
     */
    public List<PawnColour> getStudents() {
        return new ArrayList<>(students);
    }

    /**
     * If a tower is present, return a non-empty {@link SerializableOptional}
     * @return the colour of the contained tower wrapped in a {@link SerializableOptional}
     */
    public SerializableOptional<TowerColour> getTowerColour() {
        if (this.tower == null) return SerializableOptional.empty();
        else return SerializableOptional.of(this.tower.getColour());
    }

    /**
     * a student can be added to the Island through this method
     * @param p the {@link PawnColour} to add to the island
     */
    public void addStudent(PawnColour p) {
        students.add(p);
    }

    /**
     * a {@link Tower} may need to be swapped or added during the Island's lifespan, this method can be used for that
     * @param t the new {@link Tower} to be put on the island. the old tower (if any was present) will be returned to its
     *          rightful storage automatically
     */
    public void swapTower(Tower t) {
        if (this.tower != null) this.tower.linkBackToStorage();
        this.tower = t;
    }

    /* //test-purpose only
    @Override
    public String toString() {
        return "Island{" +
                "id=" + id +
                ", students=" + students +
                ", tower=" + tower +
                '}';
    }
    //*/
}
