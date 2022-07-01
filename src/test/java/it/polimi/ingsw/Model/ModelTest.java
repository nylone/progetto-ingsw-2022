package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Misc.OptionalValue;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.TeamID;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ModelTest {
    final Model gb_sim_2 = new Model(GameMode.SIMPLE, "ari", "ale");
    final Model gb_adv_3 = new Model(GameMode.ADVANCED, "ari", "ale", "teo");
    final Model gb_adv_4 = new Model(GameMode.ADVANCED, "ari", "ale", "teo", "eriantys");


    @Test
    public void testPlayerBoardId() throws InvalidContainerIndexException {
        // act
        // VALID
        PlayerBoard actual_s2_valid = gb_sim_2.getMutablePlayerBoard(0);
        PlayerBoard actual_a3_valid = gb_adv_3.getMutablePlayerBoard(1);
        PlayerBoard actual_a4_valid = gb_adv_4.getMutablePlayerBoard(2);


        // assert VALID
        assertEquals("Testing getPlayerBoardById, simple GameMode 2 people", "ari", actual_s2_valid.getNickname());
        assertEquals("Testing getPlayerBoardById, advanced GameMode 3 people", "ale", actual_a3_valid.getNickname());
        assertEquals("Testing getPlayerBoardById, advanced GameMode 4 people", "teo", actual_a4_valid.getNickname());
        // assert INVALID
        for (Model gb : List.of(gb_sim_2, gb_adv_3, gb_adv_4)) {
            try {
                // choosing an invalid player ID should result in an exception
                gb.getMutablePlayerBoard(5);
                fail("Testing getPlayerBoardById, " + gb.getGameMode() + " GameMode " + gb.getMutablePlayerBoards().size()
                        + " people, failed for invalid id");
            } catch (InvalidContainerIndexException e) {
                assertEquals("An error occurred on: Playerboards\nThe error was: provided index is out of bounds or no valid value could be retrieved.", e.getMessage());
            }
        }

    }

    @Test
    public void testPlayerBoardNickname() throws InvalidContainerIndexException {
        // act
        // VALID
        PlayerBoard actual_s2_valid = gb_sim_2.getMutablePlayerBoard(1);
        PlayerBoard actual_a3_valid = gb_adv_3.getMutablePlayerBoard(0);
        PlayerBoard actual_a4_valid = gb_adv_4.getMutablePlayerBoard(2);


        // assert VALID
        assertEquals("Testing getPlayerBoardByNickname, simple GameMode 2 people", 1, actual_s2_valid.getId());
        assertEquals("Testing getPlayerBoardByNickname, advanced GameMode 3 people", 0, actual_a3_valid.getId());
        assertEquals("Testing getPlayerBoardByNickname, advanced GameMode 4 people", 2, actual_a4_valid.getId());
        // assert INVALID
        for (Model gb : List.of(gb_sim_2, gb_adv_3, gb_adv_4)) {
            try {
                // choosing an invalid player nickname should result in an exception
                gb.getMutablePlayerBoard(-1);
                fail("Testing getPlayerBoardByNickname " + gb.getGameMode() + " GameMode " + gb.getMutablePlayerBoards().size()
                        + " people, failed for invalid nickname");
            } catch (InvalidContainerIndexException e) {
                assertEquals("An error occurred on: Playerboards\nThe error was: provided index is out of bounds or no valid value could be retrieved.", e.getMessage());
            }
        }
    }

    @Test
    public void testInfluencerOfSimpleAndAdvanced() throws NoSuchElementException, InvalidContainerIndexException {
        // arrange
        IslandGroup ig_s2 = gb_sim_2.getMutableIslandField().getMutableIslandGroupById(7);
        IslandGroup ig_a3 = gb_adv_3.getMutableIslandField().getMutableIslandGroupById(7);
        IslandGroup ig_a4 = gb_adv_4.getMutableIslandField().getMutableIslandGroupById(7);
        // adding one student because, during initialization of the game, one random student is placed on every island.
        // To be sure that the influence will be granted at least one blue student should be placed
        for (IslandGroup ig : Arrays.asList(ig_s2, ig_a3, ig_a4)) {
            ig.getMutableIslands().get(0).addStudent(PawnColour.BLUE);
        }

        // assigning blue teacher to player
        gb_sim_2.setTeacher(PawnColour.BLUE, gb_sim_2.getMutablePlayerBoard(0));
        gb_adv_3.setTeacher(PawnColour.BLUE, gb_adv_3.getMutablePlayerBoard(0));
        gb_adv_4.setTeacher(PawnColour.BLUE, gb_adv_4.getMutablePlayerBoard(0));

        // act
        // calculate influencer of island 7
        TeamID actualInfluencer_s2 = gb_sim_2.getInfluencerOf(ig_s2).get();
        TeamID actualInfluencer_a3 = gb_adv_3.getInfluencerOf(ig_a3).get();
        TeamID actualInfluencer_a4 = gb_adv_4.getInfluencerOf(ig_a4).get();

        // assert
        // checking that the influencer is the one detaining ownership of the teacher
        assertEquals(TeamID.ONE, actualInfluencer_s2);
        assertEquals(TeamID.ONE, actualInfluencer_a3);
        assertEquals(TeamID.ONE, actualInfluencer_a4);
    }

    @Test
    public void testingInfluenceOnEmptyIsland() throws Exception {
        // arrange
        // select the empty island, that is the opposite to mother nature island at the beginning
        IslandGroup empty = Utils.modularSelection(gb_sim_2.getMutableIslandField().getMutableMotherNaturePosition(),
                gb_sim_2.getMutableIslandField().getMutableGroups(), 6);
        // act
        // calculating influencer of the empty island
        OptionalValue<TeamID> actual = gb_sim_2.getInfluencerOf(empty);
        // assert
        // check that no one should control the empty island
        assertEquals(OptionalValue.empty(), actual);

    }

    /**
     * Testing that same influence on an island should keep previous influence
     */
    @Test
    public void testingInfluenceOnIslandWithSameInfluence() throws InvalidContainerIndexException {
        // arrange
        IslandGroup ig = gb_sim_2.getMutableIslandField().getMutableIslandGroupById(6);
        // placing one tower of second player on the selected island
        ig.swapTower(gb_sim_2.getTeamMapper().getMutableTowerStorage(TeamID.fromInteger(1)));


        PawnColour studentOnTheIslandAtBeginning;
        // controls that the island is not the empty or mother nature island
        if (ig.getStudents().size() != 0) {
            // select the student on the island
            studentOnTheIslandAtBeginning = ig.getStudents().get(0);
            for (PawnColour colour : PawnColour.values()) {
                if (!colour.equals(studentOnTheIslandAtBeginning)) {
                    // add 2 students on the island of a different colour from the one previously detected
                    ig.getMutableIslands().get(0).addStudent(colour);
                    ig.getMutableIslands().get(0).addStudent(colour);
                    // assign teacher to first player
                    gb_sim_2.setTeacher(colour, gb_sim_2.getMutablePlayerBoard(0));
                    break;
                }
            }
            // assign teacher to second player
            gb_sim_2.setTeacher(studentOnTheIslandAtBeginning, gb_sim_2.getMutablePlayerBoard(1));
        } else {
            // add 2 blue students and 1 red students on the selected island
            ig.getMutableIslands().get(0).addStudent(PawnColour.BLUE);
            ig.getMutableIslands().get(0).addStudent(PawnColour.BLUE);
            ig.getMutableIslands().get(0).addStudent(PawnColour.RED);
            // assign blue teacher to first player
            gb_sim_2.setTeacher(PawnColour.BLUE, gb_sim_2.getMutablePlayerBoard(0));
            // assign red teacher to second player
            gb_sim_2.setTeacher(PawnColour.RED, gb_sim_2.getMutablePlayerBoard(1));
        }

        // act
        TeamID actualInfluencer = gb_sim_2.getInfluencerOf(ig).get();

        // assert
        // verifies that, in parity situation, the second player maintains influence because he had one tower previously
        assertEquals(TeamID.TWO, actualInfluencer);
    }

    /**
     * Testing that if there are 2 players with some students on the island will win the one with more students
     */
    @Test
    public void testingInfluenceOnIslandWithStudents() throws InvalidContainerIndexException {
        // arrange
        IslandGroup ig = gb_sim_2.getMutableIslandField().getMutableIslandGroupById(6);
        // add 3 blue students to first player
        ig.getMutableIslands().get(0).addStudent(PawnColour.BLUE);
        ig.getMutableIslands().get(0).addStudent(PawnColour.BLUE);
        ig.getMutableIslands().get(0).addStudent(PawnColour.BLUE);
        // add 1 red student to second player
        ig.getMutableIslands().get(0).addStudent(PawnColour.RED);
        // assign blue teacher to first player
        gb_sim_2.setTeacher(PawnColour.BLUE, gb_sim_2.getMutablePlayerBoard(0));
        // assign red teacher to second player
        gb_sim_2.setTeacher(PawnColour.RED, gb_sim_2.getMutablePlayerBoard(1));

        // act
        TeamID actualInfluencer = gb_sim_2.getInfluencerOf(ig).get();

        // assert
        // checks that influence should be given to the player that owns the teacher whose colour is more present on the island
        assertEquals(TeamID.ONE, actualInfluencer);
    }

    /**
     * Testing that if the deny influence card is used, the denied colour should not influence the result
     */
    @Test
    public void testingInfluenceAfterCardEffect() throws InvalidContainerIndexException {
        // arrange
        // choose the denied colour as yellow
        gb_sim_2.getMutableEffects().setDeniedPawnColour(PawnColour.YELLOW);
        IslandGroup islandGroup = gb_sim_2.getMutableIslandField().getMutableIslandGroupById(6);
        // add three yellow students
        islandGroup.getMutableIslands().get(0).addStudent(PawnColour.YELLOW);
        islandGroup.getMutableIslands().get(0).addStudent(PawnColour.YELLOW);
        islandGroup.getMutableIslands().get(0).addStudent(PawnColour.YELLOW);
        // add one red student
        islandGroup.getMutableIslands().get(0).addStudent(PawnColour.RED);
        // assign yellow teacher to first player
        gb_sim_2.setTeacher(PawnColour.YELLOW, gb_sim_2.getMutablePlayerBoard(0));
        // assign red teacher to second player
        gb_sim_2.setTeacher(PawnColour.RED, gb_sim_2.getMutablePlayerBoard(1));

        // act
        TeamID actualInfluencer = gb_sim_2.getInfluencerOf(islandGroup).get();

        // assert
        // checks that influence goes to second player because of yellow colour denied
        assertEquals(TeamID.TWO, actualInfluencer);

    }

    @Test(expected = RuntimeException.class)
    public void testingInconsistentNumOfPlayers() {
        // the maximum allowed number of players is 4
        new Model(GameMode.SIMPLE, "ari", "ale", "teo", "polimi", "java");
    }

    @Test
    public void finishingTowersMakesWin() {
        // arrange & act
        Model gb = new Model(GameMode.SIMPLE, "ale", "teo", "ari");
        PlayerBoard currentPlayer = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        for (int i = 0; i < 6; i++) { // leaving current player with no towers left
            gb.getTeamMapper().getMutableTowerStorage(currentPlayer).extractTower();
        }
        // assert
        // player should win because he placed all of his towers
        assertEquals(currentPlayer, gb.getWinners().get().get(0));
    }

    @Test
    public void winWhenSameTowersButMoreTeachers() {
        // arrange & act
        Model gb = new Model(GameMode.SIMPLE, "ale", "teo", "ari");
        PlayerBoard currentPlayer = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        // assign red teacher to current player
        gb.setTeacher(PawnColour.RED, currentPlayer);
        // ends game by extracting all students from student bag
        gb.getMutableStudentBag().multipleExtraction(gb.getMutableStudentBag().getSize());
        // emptying all clouds to simulate end of last round (all players have finished their turn by selecting a cloud)
        gb.getClouds().forEach(cloud -> {
            try {
                cloud.extractContents();
            } catch (EmptyContainerException e) {
                throw new RuntimeException(e);
            }
        });

        // assert
        // current player should win because he has more teachers than the others when they have the same number of towers
        assertEquals(currentPlayer, gb.getWinners().get().get(0));
    }

    @Test
    public void parityWhenSameNumberOfTowersAndTeachers() {
        // arrange & act
        Model gb = new Model(GameMode.SIMPLE, "ale", "teo", "ari");
        // ends game by extracting all students from student bag
        gb.getMutableStudentBag().multipleExtraction(gb.getMutableStudentBag().getSize());
        // emptying all clouds to simulate end of last round (all players have finished their turn by selecting a cloud)
        gb.getClouds().forEach(cloud -> {
            try {
                cloud.extractContents();
            } catch (EmptyContainerException e) {
                throw new RuntimeException(e);
            }
        });

        // assert
        // all players are winner when game ends with parity of teachers and towers
        assertEquals(3, gb.getWinners().get().size());
    }

    @Test
    public void winShouldBeGivenToTeam() {
        // arrange & act
        Model gb = new Model(GameMode.SIMPLE, "ale", "teo", "ari", "eriantys");
        PlayerBoard currentPlayer = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        for (int i = 0; i < 8; i++) { // leaving current player with no towers left
            gb.getTeamMapper().getMutableTowerStorage(currentPlayer).extractTower();
        }
        // actual winning team
        TeamID winner = gb.getTeamMapper().getTeamID(currentPlayer);

        // assert
        // checks that all winners are in the same team of the current player
        assertTrue(gb.getWinners().get().stream().allMatch(w -> gb.getTeamMapper().getTeamID(w).equals(winner)));
        // checks that there are 2 winners because a team is made of 2 players in a 4 players game
        assertEquals(2, gb.getWinners().get().size());
    }

    @Test
    public void thereShouldBeWinnerOnlyIfGameIsEnded() {
        // arrange & act
        Model gb = new Model(GameMode.SIMPLE, "ale", "teo", "ari", "eriantys");
        // assert
        assertTrue(gb.getWinners().isEmpty());
    }

}