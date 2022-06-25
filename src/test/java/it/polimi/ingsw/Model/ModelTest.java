package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.SerializableOptional;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.TeamID;
import org.junit.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ModelTest {
    Model gb_sim_2 = new Model(GameMode.SIMPLE, "ari", "ale");
    Model gb_adv_3 = new Model(GameMode.ADVANCED, "ari", "ale", "teo");
    Model gb_adv_4 = new Model(GameMode.ADVANCED, "ari", "ale", "teo", "eriantys");


    @Test
    public void testPlayerBoardId() throws InvalidContainerIndexException {
        // act
        // VALID
        PlayerBoard actual_s2_valid = gb_sim_2.getMutablePlayerBoardById(0);
        PlayerBoard actual_a3_valid = gb_adv_3.getMutablePlayerBoardById(1);
        PlayerBoard actual_a4_valid = gb_adv_4.getMutablePlayerBoardById(2);


        // assert VALID
        assertEquals("Testing getPlayerBoardById, simple GameMode 2 people", "ari", actual_s2_valid.getNickname());
        assertEquals("Testing getPlayerBoardById, advanced GameMode 3 people", "ale", actual_a3_valid.getNickname());
        assertEquals("Testing getPlayerBoardById, advanced GameMode 4 people", "teo", actual_a4_valid.getNickname());
        // assert INVALID
        try {
            PlayerBoard actual_s2_invalid = gb_sim_2.getMutablePlayerBoardById(5);
            fail("Testing getPlayerBoardById, simple GameMode 2 people, failed for invalid id");
            PlayerBoard actual_a3_invalid = gb_adv_3.getMutablePlayerBoardById(27);
            fail("Testing getPlayerBoardById, advanced GameMode 3 people, failed for invalid id");
            PlayerBoard actual_a4_invalid = gb_adv_4.getMutablePlayerBoardById(323);
            fail("Testing getPlayerBoardById, advanced GameMode 4 people, failed for invalid id");
        } catch (InvalidContainerIndexException e) {
            assertEquals("An error occurred on: Playerboards\nThe error was: provided index is out of bounds or no valid value could be retrieved.", e.getMessage());
        }
    }

    @Test
    public void testPlayerBoardNickname() throws InvalidContainerIndexException {
        // act
        // VALID
        PlayerBoard actual_s2_valid = gb_sim_2.getMutablePlayerBoardByNickname("ale");
        PlayerBoard actual_a3_valid = gb_adv_3.getMutablePlayerBoardByNickname("ari");
        PlayerBoard actual_a4_valid = gb_adv_4.getMutablePlayerBoardByNickname("teo");


        // assert VALID
        assertEquals("Testing getPlayerBoardByNickname, simple GameMode 2 people", 1, actual_s2_valid.getId());
        assertEquals("Testing getPlayerBoardByNickname, advanced GameMode 3 people", 0, actual_a3_valid.getId());
        assertEquals("Testing getPlayerBoardByNickname, advanced GameMode 4 people", 2, actual_a4_valid.getId());
        // assert INVALID
        try {
            PlayerBoard actual_s2_invalid = gb_sim_2.getMutablePlayerBoardByNickname("wrong");
            fail("Testing getPlayerBoardByNickname, simple GameMode 2 people, failed for invalid nickname");
            PlayerBoard actual_a3_invalid = gb_adv_3.getMutablePlayerBoardByNickname("wrong");
            fail("Testing getPlayerBoardByNickname, advanced GameMode 3 people, failed for invalid nickname");
            PlayerBoard actual_a4_invalid = gb_adv_4.getMutablePlayerBoardByNickname("wrong");
            fail("Testing getPlayerBoardByNickname, advanced GameMode 4 people, failed for invalid nickname");
        } catch (InvalidContainerIndexException e) {
            assertEquals("An error occurred on: Playerboards\nThe error was: provided index is out of bounds or no valid value could be retrieved.", e.getMessage());
        }
    }

    @Test
    public void testInfluencerOfSimpleAndAdvanced() throws NoSuchElementException, InvalidContainerIndexException {
        // arrange
        IslandGroup ig_s2 = gb_sim_2.getMutableIslandField().getMutableIslandGroupById(7);
        IslandGroup ig_a3 = gb_adv_3.getMutableIslandField().getMutableIslandGroupById(7);
        IslandGroup ig_a4 = gb_adv_4.getMutableIslandField().getMutableIslandGroupById(7);
        // adding two students because, during initialization of the game, one random students is placed on the islands
        // to be sure that the influence will be granted at least two students should be placed
        for (IslandGroup ig : Arrays.asList(ig_s2, ig_a3, ig_a4)) {
            ig.getMutableIslands().get(0).addStudent(PawnColour.BLUE);
        }

        gb_sim_2.setTeacher(PawnColour.BLUE, gb_sim_2.getMutablePlayerBoardByNickname("ari"));
        gb_adv_3.setTeacher(PawnColour.BLUE, gb_adv_3.getMutablePlayerBoardByNickname("ari"));
        gb_adv_4.setTeacher(PawnColour.BLUE, gb_adv_4.getMutablePlayerBoardByNickname("ari"));
        // act
        TeamID actualInfluencer_s2 = gb_sim_2.getInfluencerOf(ig_s2).get();
        TeamID actualInfluencer_a3 = gb_adv_3.getInfluencerOf(ig_a3).get();
        TeamID actualInfluencer_a4 = gb_adv_4.getInfluencerOf(ig_a4).get();
        // assert
        assertEquals(TeamID.ONE, actualInfluencer_s2);
        assertEquals(TeamID.ONE, actualInfluencer_a3);
        assertEquals(TeamID.ONE, actualInfluencer_a4);
    }

    @Test
    public void testingInfluenceOnEmptyIsland() throws Exception {
        // arrange
        IslandGroup empty = Utils.modularSelection(gb_sim_2.getMutableIslandField().getMutableMotherNaturePosition(),
                gb_sim_2.getMutableIslandField().getMutableGroups(), 6);
        // act
        SerializableOptional<TeamID> actual = gb_sim_2.getInfluencerOf(empty);
        // assert
        assertEquals(SerializableOptional.empty(), actual);

    }

    /**
     * Testing that same influence on an island should keep previous influence
     */
    @Test
    public void testingInfluenceOnIslandWithSameInfluence() throws InvalidContainerIndexException {
        // arrange
        IslandGroup ig = gb_sim_2.getMutableIslandField().getMutableIslandGroupById(6);
        ig.getMutableIslands().get(0).swapTower(gb_sim_2.getTeamMapper().getMutableTowerStorage(TeamID.fromInteger(1)).extractTower());

        PawnColour studentOnTheIslandAtBeginning;
        if (ig.getStudents().size() != 0) {
            studentOnTheIslandAtBeginning = ig.getStudents().get(0);
            for (PawnColour colour : PawnColour.values()) {
                if (!colour.equals(studentOnTheIslandAtBeginning)) {
                    ig.getMutableIslands().get(0).addStudent(colour);
                    ig.getMutableIslands().get(0).addStudent(colour);
                    gb_sim_2.setTeacher(colour, gb_sim_2.getMutablePlayerBoardByNickname("ari"));
                    break;
                }
            }
            gb_sim_2.setTeacher(studentOnTheIslandAtBeginning, gb_sim_2.getMutablePlayerBoardByNickname("ale"));
        } else {
            ig.getMutableIslands().get(0).addStudent(PawnColour.BLUE);
            ig.getMutableIslands().get(0).addStudent(PawnColour.BLUE);
            ig.getMutableIslands().get(0).addStudent(PawnColour.RED);
            gb_sim_2.setTeacher(PawnColour.BLUE, gb_sim_2.getMutablePlayerBoardByNickname("ari"));
            gb_sim_2.setTeacher(PawnColour.RED, gb_sim_2.getMutablePlayerBoardByNickname("ale"));
        }
        // act
        TeamID actualInfluencer = gb_sim_2.getInfluencerOf(ig).get();
        // assert
        assertEquals(TeamID.TWO, actualInfluencer);
    }

    /**
     * Testing that if there are 2 players with some students on the island will win the one with more students
     */
    @Test
    public void testingInfluenceOnIslandWithStudents() throws InvalidContainerIndexException {
        // arrange
        IslandGroup ig = gb_sim_2.getMutableIslandField().getMutableIslandGroupById(6);
        ig.getMutableIslands().get(0).addStudent(PawnColour.BLUE);
        ig.getMutableIslands().get(0).addStudent(PawnColour.BLUE);
        ig.getMutableIslands().get(0).addStudent(PawnColour.BLUE);
        ig.getMutableIslands().get(0).addStudent(PawnColour.RED);
        gb_sim_2.setTeacher(PawnColour.BLUE, gb_sim_2.getMutablePlayerBoardByNickname("ari"));
        gb_sim_2.setTeacher(PawnColour.RED, gb_sim_2.getMutablePlayerBoardByNickname("ale"));
        // act
        TeamID actualInfluencer = gb_sim_2.getInfluencerOf(ig).get();
        // assert
        assertEquals(TeamID.ONE, actualInfluencer);
    }

    /**
     * Testing that if the deny influence card is used, the denied colour should not influence the result
     */
    @Test
    public void testingInfluenceAfterCardEffect() throws InvalidContainerIndexException {
        // arrange
        gb_sim_2.getMutableEffects().setDeniedPawnColour(PawnColour.YELLOW);
        IslandGroup islandGroup = gb_sim_2.getMutableIslandField().getMutableIslandGroupById(6);
        islandGroup.getMutableIslands().get(0).addStudent(PawnColour.YELLOW);
        islandGroup.getMutableIslands().get(0).addStudent(PawnColour.YELLOW);
        islandGroup.getMutableIslands().get(0).addStudent(PawnColour.YELLOW);
        islandGroup.getMutableIslands().get(0).addStudent(PawnColour.RED);
        gb_sim_2.setTeacher(PawnColour.YELLOW, gb_sim_2.getMutablePlayerBoardByNickname("ari"));
        gb_sim_2.setTeacher(PawnColour.RED, gb_sim_2.getMutablePlayerBoardByNickname("ale"));
        // act
        TeamID actualInfluencer = gb_sim_2.getInfluencerOf(islandGroup).get();
        // assert
        assertEquals(TeamID.TWO, actualInfluencer);

    }

    @Test(expected = RuntimeException.class)
    public void testingInconsistentNumOfPlayers() {
        Model gb_adv_5 = new Model(GameMode.SIMPLE, "ari", "ale", "teo", "polimi", "java");
    }

    @Test
    public void finishingTowersMakesWin() throws InputValidationException {
        // arrange & act
        Model gb = new Model(GameMode.SIMPLE, "ale", "teo", "ari");
        PlayerBoard currentPlayer = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        for (int i = 0; i < 6; i++) { // leaving current player with just one tower left
            gb.getTeamMapper().getMutableTowerStorage(currentPlayer).extractTower();
        }
        // assert
        assertEquals(currentPlayer, gb.getWinners().get().get(0));
    }

    @Test
    public void winWhenSameTowersButMoreTeachers() {
        // arrange & act
        Model gb = new Model(GameMode.SIMPLE, "ale", "teo", "ari");
        PlayerBoard currentPlayer = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        gb.setTeacher(PawnColour.RED, currentPlayer);
        // ends game
        gb.getMutableStudentBag().multipleExtraction(gb.getMutableStudentBag().getSize());
        gb.getClouds().forEach(cloud -> {
            try {
                cloud.extractContents();
            } catch (EmptyContainerException e) {
                throw new RuntimeException(e);
            }
        });
        // assert
        assertEquals(currentPlayer, gb.getWinners().get().get(0));
    }

    @Test
    public void parityWhenSameNumberOfTowersAndTeachers() {
        // arrange & act
        Model gb = new Model(GameMode.SIMPLE, "ale", "teo", "ari");
        PlayerBoard currentPlayer = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        // ends game
        gb.getMutableStudentBag().multipleExtraction(gb.getMutableStudentBag().getSize());
        gb.getClouds().forEach(cloud -> {
            try {
                cloud.extractContents();
            } catch (EmptyContainerException e) {
                throw new RuntimeException(e);
            }
        });
        // assert
        assertTrue(gb.getWinners().get().size() == 3);
    }

    @Test
    public void winShouldBeGivenToTeam() {
        // arrange & act
        Model gb = new Model(GameMode.SIMPLE, "ale", "teo", "ari", "eriantys");
        PlayerBoard currentPlayer = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        for (int i = 0; i < 8; i++) { // leaving current player with no towers left
            gb.getTeamMapper().getMutableTowerStorage(currentPlayer).extractTower();
        }
        // assert
        PlayerBoard winner = gb.getWinners().get().get(0);
        assertEquals(gb.getTeamMapper().getTeamID(currentPlayer), gb.getTeamMapper().getTeamID(winner));
        assertTrue(gb.getWinners().get().size() == 2);
    }

    @Test
    public void thereShouldBeWinnerOnlyIfGameIsEnded() {
        // arrange & act
        Model gb = new Model(GameMode.SIMPLE, "ale", "teo", "ari", "eriantys");
        // assert
        assertTrue(gb.getWinners().isEmpty());
    }

}