package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Operation.OperationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.TeamID;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class IslandGroupTest {

    @Test
    public void testGetStudentCount() {
        // arrange
        Island island = new Island(4);
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.BLUE);
        island.addStudent(PawnColour.YELLOW);
        IslandGroup islandGroup = new IslandGroup(island);
        // act
        int actualBlue = islandGroup.getStudentCount().get(PawnColour.BLUE);
        int actualYellow = islandGroup.getStudentCount().get(PawnColour.YELLOW);
        // assert
        assertEquals(2, actualBlue);
        assertEquals(1, actualYellow);
    }

    @Test
    public void testGetStudents() throws OperationException {
        // arrange
        Model gb = new Model(GameMode.SIMPLE, "ale", "teo");
        TeamMapper tm = gb.getTeamMapper();
        Island i2 = new Island(2);
        Island i3 = new Island(3);
        // add blue and red students to second island
        i2.addStudent(PawnColour.BLUE);
        i2.addStudent(PawnColour.RED);
        // add green student to third island
        i3.addStudent(PawnColour.GREEN);

        // adds tower of same colour to both selected islands
        i2.swapTower(tm.getMutableTowerStorage(TeamID.ONE).extractTower());
        i3.swapTower(tm.getMutableTowerStorage(TeamID.ONE).extractTower());

        // put the islands in an island group respectively to enable joining functionality
        IslandGroup islandGroup1 = new IslandGroup(i2);
        IslandGroup islandGroup2 = new IslandGroup(i3);
        // join groups together
        IslandGroup islandGroup = new IslandGroup(islandGroup1, islandGroup2);

        // this is what the joined island should contain
        List<PawnColour> expected = new ArrayList<>(Arrays.asList(PawnColour.BLUE, PawnColour.RED, PawnColour.GREEN));

        // act
        // takes the student actually contained in the joined island
        List<PawnColour> actual = islandGroup.getStudents();

        // assert
        assertEquals(expected, actual);
        assertEquals(3, actual.size());
        assertEquals(1, (int) islandGroup1.getStudentCount().get(PawnColour.BLUE));
    }
}