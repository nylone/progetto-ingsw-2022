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
        int expected = (initialPosition + islandField.getMutableGroups().size() + movement) % islandField.getMutableGroups().size();
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
            IslandGroup found = field.getMutableIslandGroupById(15);
            fail("An exception was thrown");
        } catch (InvalidContainerIndexException e) {
            // assert
            assertEquals("An error occurred on: IslandField.groups\nThe error was: provided index is out of bounds or no valid value could be retrieved.", e.getMessage());
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
            field.getMutableIslandById(18);
            fail("Exception was thrown");
        } catch (InvalidContainerIndexException e) {
            // assert
            assertEquals("An error occurred on: IslandField.islands\nThe error was: provided index is out of bounds or no valid value could be retrieved.", e.getMessage());
        }
    }

    @Test
    public void testingJoiningMotherNatureWithPreviousGroup() throws Exception {
        // arrange
        GameBoard gameBoard = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        IslandGroup motherNaturePosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        List<IslandGroup> groups = gameBoard.getMutableIslandField().getMutableGroups();
        IslandGroup previousGroup = Utils.modularSelection(motherNaturePosition, groups, -1);

        TeamMapper teamMapper = gameBoard.getTeamMapper();
        motherNaturePosition.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());
        previousGroup.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());
        // act
        gameBoard.getMutableIslandField().joinGroups();
        // assert
        IslandGroup currentMotherNaturePosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        assertEquals(2, currentMotherNaturePosition.getMutableIslands().size());
        assertEquals(motherNaturePosition.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(0));
        assertEquals(previousGroup.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(1));
    }

    @Test
    public void testingJoiningMotherNatureWithNextGroup() throws Exception {
        // arrange
        GameBoard gameBoard = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        IslandGroup motherNaturePosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        List<IslandGroup> groups = gameBoard.getMutableIslandField().getMutableGroups();
        IslandGroup nextGroup = Utils.modularSelection(motherNaturePosition, groups, +1);

        TeamMapper teamMapper = gameBoard.getTeamMapper();
        motherNaturePosition.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());
        nextGroup.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());
        // act
        gameBoard.getMutableIslandField().joinGroups();
        // assert
        IslandGroup currentMotherNaturePosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        assertEquals(2, currentMotherNaturePosition.getMutableIslands().size());
        assertEquals(motherNaturePosition.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(0));
        assertEquals(nextGroup.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(1));
    }

    @Test
    public void testingJoiningThreeIslands() throws Exception {
        // arrange
        GameBoard gameBoard = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        IslandGroup motherNaturePosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        List<IslandGroup> groups = gameBoard.getMutableIslandField().getMutableGroups();
        IslandGroup prevGroup = Utils.modularSelection(motherNaturePosition, groups, -1);
        IslandGroup nextGroup = Utils.modularSelection(motherNaturePosition, groups, +1);

        TeamMapper teamMapper = gameBoard.getTeamMapper();
        motherNaturePosition.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());
        prevGroup.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());
        nextGroup.getMutableIslands().get(0).swapTower(teamMapper.getMutableTowerStorage(TeamID.ONE).extractTower());
        // act
        gameBoard.getMutableIslandField().joinGroups();
        // assert
        IslandGroup currentMotherNaturePosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        assertEquals(3, currentMotherNaturePosition.getMutableIslands().size());
        assertEquals(motherNaturePosition.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(0));
        assertEquals(prevGroup.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(1));
        assertEquals(nextGroup.getMutableIslands().get(0), currentMotherNaturePosition.getMutableIslands().get(2));
    }
}