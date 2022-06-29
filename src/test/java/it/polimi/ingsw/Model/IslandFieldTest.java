package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.TeamID;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class IslandFieldTest {
    private final IslandField field = new IslandField();

    @Test
    public void motherNatureMovementIsCorrect() {
        // arrange
        IslandField islandField = new IslandField();
        int initialPosition = islandField.getMutableMotherNaturePosition().getId();
        int movement = new Random().nextInt(0, 12);
        // act
        islandField.moveMotherNature(movement);
        // assert
        // mod 12 arithmetic is needed to calculate final mother nature position (because there are 12 islands initially)
        int expected = (initialPosition + movement + 12) % 12;
        assertEquals(expected, islandField.getMutableMotherNaturePosition().getId());
    }

    @Test
    public void testValidIslandGroupById() throws InvalidContainerIndexException {
        // arrange
        IslandField field = new IslandField();

        // act
        IslandGroup found = field.getMutableIslandGroupById(1);

        // assert
        assertEquals(1, found.getId());
    }

    @Test
    public void testInvalidIslandGroupId() {
        // arrange
        IslandField field = new IslandField();

        try {
            // act
            // trying to access not existent island group
            field.getMutableIslandGroupById(15);
            fail("An exception was thrown");
        } catch (InvalidContainerIndexException e) {
            // assert
            assertEquals("An error occurred on: Island Groups\nThe error was: provided index is out of bounds or no valid value could be retrieved.", e.getMessage());
        }
    }

    @Test
    public void testValidIslandId() throws InvalidContainerIndexException {
        // act
        Island actualIsland = field.getMutableIslandById(2);
        // assert
        assertEquals(2, actualIsland.getId());
    }

    @Test
    public void testInvalidIslandId() {
        // act
        try {
            // trying to access not existent island
            field.getMutableIslandById(18);
            fail("Exception was thrown");
        } catch (InvalidContainerIndexException e) {
            // assert
            assertEquals("An error occurred on: Islands\nThe error was: provided index is out of bounds or no valid value could be retrieved.", e.getMessage());
        }
    }

    @Test
    public void testingJoiningMotherNatureWithPreviousGroup() throws Exception {
        // arrange
        Model model = new Model(GameMode.SIMPLE, "ale", "teo");
        // selects island containing mother nature
        IslandGroup initialMnPosition = model.getMutableIslandField().getMutableMotherNaturePosition();
        List<IslandGroup> groups = model.getMutableIslandField().getMutableGroups();
        // selects island before mother nature island
        IslandGroup previousGroup = Utils.modularSelection(initialMnPosition, groups, -1);

        // adds tower of same colour to both selected islands
        TeamMapper teamMapper = model.getTeamMapper();
        initialMnPosition.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());
        previousGroup.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());

        // act
        model.getMutableIslandField().joinGroups();

        // assert
        // update mother nature position
        IslandGroup currentMotherNaturePosition = model.getMutableIslandField().getMutableMotherNaturePosition();
        // verifies that 2 islands have been merged in one island group
        assertEquals(2, currentMotherNaturePosition.getMutableIslands().size());
        // checks that the first island in the joined group is the initial mother nature island
        assertEquals(initialMnPosition.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(0));
        // check that the second island in the joined group is the island before initial mother nature island
        assertEquals(previousGroup.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(1));
    }

    @Test
    public void testingJoiningMotherNatureWithNextGroup() throws Exception {
        // arrange
        Model model = new Model(GameMode.SIMPLE, "ale", "teo");
        // selects island containing mother nature
        IslandGroup motherNaturePosition = model.getMutableIslandField().getMutableMotherNaturePosition();
        List<IslandGroup> groups = model.getMutableIslandField().getMutableGroups();
        // selects island after mother nature island
        IslandGroup nextGroup = Utils.modularSelection(motherNaturePosition, groups, +1);

        // adds tower of same colour to both selected islands
        TeamMapper teamMapper = model.getTeamMapper();
        motherNaturePosition.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());
        nextGroup.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());

        // act
        model.getMutableIslandField().joinGroups();

        // assert
        // update mother nature position
        IslandGroup currentMotherNaturePosition = model.getMutableIslandField().getMutableMotherNaturePosition();
        // verifies that 2 islands have been merged in one island group
        assertEquals(2, currentMotherNaturePosition.getMutableIslands().size());
        // checks that the first island in the joined group is the initial mother nature island
        assertEquals(motherNaturePosition.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(0));
        // check that the second island in the joined group is the island after initial mother nature island
        assertEquals(nextGroup.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(1));
    }

    @Test
    public void testingJoiningThreeIslands() throws Exception {
        // arrange
        Model model = new Model(GameMode.SIMPLE, "ale", "teo");
        // selects island containing mother nature
        IslandGroup motherNaturePosition = model.getMutableIslandField().getMutableMotherNaturePosition();
        List<IslandGroup> groups = model.getMutableIslandField().getMutableGroups();
        // selects island before mother nature island
        IslandGroup prevGroup = Utils.modularSelection(motherNaturePosition, groups, -1);
        // selects island after mother nature island
        IslandGroup nextGroup = Utils.modularSelection(motherNaturePosition, groups, +1);

        // adds tower of same colour to the three selected islands
        TeamMapper teamMapper = model.getTeamMapper();
        motherNaturePosition.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());
        prevGroup.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());
        nextGroup.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());

        // act
        model.getMutableIslandField().joinGroups();

        // assert
        // update mother nature position
        IslandGroup currentMotherNaturePosition = model.getMutableIslandField().getMutableMotherNaturePosition();
        // verifies that 3 islands have been merged in one island group
        assertEquals(3, currentMotherNaturePosition.getMutableIslands().size());
        // checks that the first island in the joined group is the initial mother nature island
        assertEquals(motherNaturePosition.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(0));
        // check that the second island in the joined group is the island before initial mother nature island
        assertEquals(prevGroup.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(1));
        // check that the third island in the joined group is the island after initial mother nature island
        assertEquals(nextGroup.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(2));
    }
}