package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.toremove.InvalidInputException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.TeamID;
import org.junit.Test;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.Assert.*;

public class GameBoardTest {
    GameBoard gb_sim_2 = new GameBoard(GameMode.SIMPLE, "ari", "ale");
    GameBoard gb_adv_3 = new GameBoard(GameMode.ADVANCED, "ari", "ale", "teo");
    GameBoard gb_adv_4 = new GameBoard(GameMode.ADVANCED, "ari", "ale", "teo", "eriantys");



    @Test
    public void testPlayerBoardId() throws InvalidContainerIndexException {
        // act
        // VALID
        PlayerBoard actual_s2_valid = gb_sim_2.getMutablePlayerBoardById(1);
        PlayerBoard actual_a3_valid = gb_adv_3.getMutablePlayerBoardById(2);
        PlayerBoard actual_a4_valid = gb_adv_4.getMutablePlayerBoardById(3);


        // assert VALID
        assertTrue("Testing getPlayerBoardById, simple GameMode 2 people", actual_s2_valid.getNickname().equals("ari"));
        assertTrue("Testing getPlayerBoardById, advanced GameMode 3 people", actual_a3_valid.getNickname().equals("ale"));
        assertTrue("Testing getPlayerBoardById, advanced GameMode 4 people", actual_a4_valid.getNickname().equals("teo"));
        // assert INVALID
        try {
            PlayerBoard actual_s2_invalid = gb_sim_2.getMutablePlayerBoardById(5);
            fail("Testing getPlayerBoardById, simple GameMode 2 people, failed for invalid id");
            PlayerBoard actual_a3_invalid = gb_adv_3.getMutablePlayerBoardById(27);
            fail("Testing getPlayerBoardById, advanced GameMode 3 people, failed for invalid id");
            PlayerBoard actual_a4_invalid = gb_adv_4.getMutablePlayerBoardById(323);
            fail("Testing getPlayerBoardById, advanced GameMode 4 people, failed for invalid id");
        }
        catch(InvalidInputException e) {
            assertEquals("Invalid input provided", e.getMessage());
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
        assertTrue("Testing getPlayerBoardByNickname, simple GameMode 2 people", actual_s2_valid.getId() == 2);
        assertTrue("Testing getPlayerBoardByNickname, advanced GameMode 3 people", actual_a3_valid.getId() == 1);
        assertTrue("Testing getPlayerBoardByNickname, advanced GameMode 4 people", actual_a4_valid.getId() == 3);
        // assert INVALID
        try {
            PlayerBoard actual_s2_invalid = gb_sim_2.getMutablePlayerBoardByNickname("wrong");
            fail("Testing getPlayerBoardByNickname, simple GameMode 2 people, failed for invalid nickname");
            PlayerBoard actual_a3_invalid = gb_adv_3.getMutablePlayerBoardByNickname("wrong");
            fail("Testing getPlayerBoardByNickname, advanced GameMode 3 people, failed for invalid nickname");
            PlayerBoard actual_a4_invalid = gb_adv_4.getMutablePlayerBoardByNickname("wrong");
            fail("Testing getPlayerBoardByNickname, advanced GameMode 4 people, failed for invalid nickname");
        }
        catch(InvalidInputException e) {
            assertEquals("Invalid input provided", e.getMessage());
        }
    }

    @Test
    public void testInfluencerOfSimpleAndAdvanced() throws NoSuchElementException, InvalidContainerIndexException {
        // arrange
        IslandGroup ig_s2 = gb_sim_2.getMutableIslandField().getIslandGroupById(7);
        IslandGroup ig_a3 = gb_adv_3.getMutableIslandField().getIslandGroupById(7);
        IslandGroup ig_a4 = gb_adv_4.getMutableIslandField().getIslandGroupById(7);
        // adding two students because, during initialization of the game, one random students is placed on the islands
        // to be sure that the influence will be granted at least two students should be placed
        for (IslandGroup ig : Arrays.asList(ig_s2, ig_a3, ig_a4)) {
            ig.getIslands().get(0).addStudent(PawnColour.BLUE);
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
    public void testingInfluenceOnEmptyIsland() {
        // arrange
        IslandGroup empty = Utils.modularSelection(gb_sim_2.getMutableIslandField().getMotherNaturePosition(),
                gb_sim_2.getMutableIslandField().getGroups(), 6);
        // act
        Optional<TeamID> actual = gb_sim_2.getInfluencerOf(empty);
        // assert
        assertEquals(Optional.empty(), actual);

    }

    /**
     * Testing that same influence on an island should keep previous influence
     */
    @Test
    public void testingInfluenceOnIslandWithSameInfluence() throws InvalidContainerIndexException {
        // arrange
        IslandGroup ig = gb_sim_2.getMutableIslandField().getIslandGroupById(6);
        ig.getIslands().get(0).swapTower(gb_sim_2.getTeamMap().getTowerStorage(TeamID.fromInteger(1)).extractTower());

        PawnColour studentOnTheIslandAtBeginning;
        if (ig.getStudents().size() != 0) {
            studentOnTheIslandAtBeginning = ig.getStudents().get(0);
            for (PawnColour colour : PawnColour.values()) {
                if (colour.equals(studentOnTheIslandAtBeginning)) continue;
                else {
                    ig.getIslands().get(0).addStudent(colour);
                    ig.getIslands().get(0).addStudent(colour);
                    gb_sim_2.setTeacher(colour, gb_sim_2.getMutablePlayerBoardByNickname("ari"));
                    break;
                }
            }
            gb_sim_2.setTeacher(studentOnTheIslandAtBeginning, gb_sim_2.getMutablePlayerBoardByNickname("ale"));
        }
        else {
            ig.getIslands().get(0).addStudent(PawnColour.BLUE);
            ig.getIslands().get(0).addStudent(PawnColour.BLUE);
            ig.getIslands().get(0).addStudent(PawnColour.RED);
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
        IslandGroup ig = gb_sim_2.getMutableIslandField().getIslandGroupById(6);
        ig.getIslands().get(0).addStudent(PawnColour.BLUE);
        ig.getIslands().get(0).addStudent(PawnColour.BLUE);
        ig.getIslands().get(0).addStudent(PawnColour.BLUE);
        ig.getIslands().get(0).addStudent(PawnColour.RED);
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
        IslandGroup islandGroup = gb_sim_2.getMutableIslandField().getIslandGroupById(6);
        islandGroup.getIslands().get(0).addStudent(PawnColour.YELLOW);
        islandGroup.getIslands().get(0).addStudent(PawnColour.YELLOW);
        islandGroup.getIslands().get(0).addStudent(PawnColour.YELLOW);
        islandGroup.getIslands().get(0).addStudent(PawnColour.RED);
        gb_sim_2.setTeacher(PawnColour.YELLOW, gb_sim_2.getMutablePlayerBoardByNickname("ari"));
        gb_sim_2.setTeacher(PawnColour.RED, gb_sim_2.getMutablePlayerBoardByNickname("ale"));
        // act
        TeamID actualInfluencer = gb_sim_2.getInfluencerOf(islandGroup).get();
        // assert
        assertEquals(TeamID.TWO, actualInfluencer);

    }

    @Test(expected = RuntimeException.class)
    public void testingIncostintentNumOfPlyaers(){
        GameBoard gb_adv_5 = new GameBoard(GameMode.SIMPLE, "ari", "ale", "teo", "polimi", "java");
    }
}