package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.toremove.InvalidInputException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.TeamID;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class IslandFieldTest {
    private IslandField field = new IslandField();

    @Test
    public void motherNatureMovementIsCorrect() {
        // arrange
        IslandField islandField = new IslandField();
        int initialPosition = islandField.getMotherNaturePosition().getId();
        int movement = new Random().nextInt(0, 12);
        // act
        islandField.moveMotherNature(movement);
        // assert
        int expected = (initialPosition + islandField.getGroups().size() + movement) % islandField.getGroups().size();
        assertEquals(expected, islandField.getMotherNaturePosition().getId());
    }

    @Test
    public void testValidIslandGroupById() {
        // arrange
        IslandField field = new IslandField();

        // act
        IslandGroup found = field.getIslandGroupById(1);

        // assert
        assertTrue(found.getId() == 1);
    }

    @Test
    public void testInvalidIslandGroupId() {
        // arrange
        IslandField field = new IslandField();

        try {
            // act
            IslandGroup found = field.getIslandGroupById(15);
            fail("An exception was thrown");
        } catch (InvalidInputException e) {
            // assert
            assertEquals("IslandGroup.id problem: id out of bound", e.getMessage());
        }
    }

    @Test
    public void testValidIslandId() {
        // act
        Island actualIsland = field.getIslandById(2);
        // assert
        assertTrue(actualIsland.getId() == 2);
    }

    @Test
    public void testInvalidIslandId() {
        // act
        try {
            field.getIslandById(18);
            fail("Exception was thrown");
        } catch (InvalidInputException e) {
            // assert
            assertEquals("Island.id problem: id out of bound", e.getMessage());
        }
    }

    @Test
    public void testingJoiningMotherNatureWithPreviousGroup() {
        // arrange
        GameBoard gameBoard = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        IslandGroup motherNaturePosition = gameBoard.getIslandField().getMotherNaturePosition();
        ArrayList<IslandGroup> groups = gameBoard.getIslandField().getGroups();
        IslandGroup previousGroup = Utils.modularSelection(motherNaturePosition, groups, -1);

        TeamMapper teamMapper = gameBoard.getTeamMap();
        motherNaturePosition.getIslands().get(0).swapTower(teamMapper.getTowerStorage(TeamID.ONE).extractTower());
        previousGroup.getIslands().get(0).swapTower(teamMapper.getTowerStorage(TeamID.ONE).extractTower());
        // act
        gameBoard.getIslandField().joinGroups();
        // assert
        IslandGroup currentMotherNaturePosition = gameBoard.getIslandField().getMotherNaturePosition();
        assertTrue(currentMotherNaturePosition.getIslands().size() == 2);
        assertEquals(motherNaturePosition.getIslands().get(0), currentMotherNaturePosition.getIslands().get(0));
        assertEquals(previousGroup.getIslands().get(0), currentMotherNaturePosition.getIslands().get(1));
    }

    @Test
    public void testingJoiningMotherNatureWithNextGroup() {
        // arrange
        GameBoard gameBoard = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        IslandGroup motherNaturePosition = gameBoard.getIslandField().getMotherNaturePosition();
        ArrayList<IslandGroup> groups = gameBoard.getIslandField().getGroups();
        IslandGroup nextGroup = Utils.modularSelection(motherNaturePosition, groups, +1);

        TeamMapper teamMapper = gameBoard.getTeamMap();
        motherNaturePosition.getIslands().get(0).swapTower(teamMapper.getTowerStorage(TeamID.ONE).extractTower());
        nextGroup.getIslands().get(0).swapTower(teamMapper.getTowerStorage(TeamID.ONE).extractTower());
        // act
        gameBoard.getIslandField().joinGroups();
        // assert
        IslandGroup currentMotherNaturePosition = gameBoard.getIslandField().getMotherNaturePosition();
        assertTrue(currentMotherNaturePosition.getIslands().size() == 2);
        assertEquals(motherNaturePosition.getIslands().get(0), currentMotherNaturePosition.getIslands().get(0));
        assertEquals(nextGroup.getIslands().get(0), currentMotherNaturePosition.getIslands().get(1));
    }

    @Test
    public void testingJoiningThreeIslands() {
        // arrange
        GameBoard gameBoard = new GameBoard(GameMode.SIMPLE, "ale", "teo");
        IslandGroup motherNaturePosition = gameBoard.getIslandField().getMotherNaturePosition();
        ArrayList<IslandGroup> groups = gameBoard.getIslandField().getGroups();
        IslandGroup prevGroup = Utils.modularSelection(motherNaturePosition, groups, -1);
        IslandGroup nextGroup = Utils.modularSelection(motherNaturePosition, groups, +1);

        TeamMapper teamMapper = gameBoard.getTeamMap();
        motherNaturePosition.getIslands().get(0).swapTower(teamMapper.getTowerStorage(TeamID.ONE).extractTower());
        prevGroup.getIslands().get(0).swapTower(teamMapper.getTowerStorage(TeamID.ONE).extractTower());
        nextGroup.getIslands().get(0).swapTower(teamMapper.getTowerStorage(TeamID.ONE).extractTower());
        // act
        gameBoard.getIslandField().joinGroups();
        // assert
        IslandGroup currentMotherNaturePosition = gameBoard.getIslandField().getMotherNaturePosition();
        assertTrue(currentMotherNaturePosition.getIslands().size() == 3);
        assertEquals(motherNaturePosition.getIslands().get(0), currentMotherNaturePosition.getIslands().get(0));
        assertEquals(prevGroup.getIslands().get(0), currentMotherNaturePosition.getIslands().get(1));
        assertEquals(nextGroup.getIslands().get(0), currentMotherNaturePosition.getIslands().get(2));
    }
}