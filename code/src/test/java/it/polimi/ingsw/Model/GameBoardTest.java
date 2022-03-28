package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;
import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameBoardTest {
    GameBoard gb_sim_2 = new GameBoard(GameMode.SIMPLE, "ari", "ale");
    //GameBoard gb_adv_3 = new GameBoard(GameMode.ADVANCED, "ari", "ale", "teo");
    //GameBoard gb_adv_4 = new GameBoard(GameMode.ADVANCED, "ari", "ale", "teo", "eriantys");
    @Test
    public void testPlayerBoardId() {
        // act
        // VALID
        PlayerBoard actual_s2_valid = gb_sim_2.getPlayerBoardById(1);
        //PlayerBoard actual_a3_valid = gb_adv_3.getPlayerBoardById(2);
        //PlayerBoard actual_a4_valid = gb_adv_4.getPlayerBoardById(3);


        // assert VALID
        assertTrue("Testing getPlayerBoardById, simple GameMode 2 people", actual_s2_valid.getNickname().equals("ari"));
        //assertTrue("Testing getPlayerBoardById, advanced GameMode 3 people", actual_s2_valid.getNickname().equals("ale"));
        //assertTrue("Testing getPlayerBoardById, advanced GameMode 4 people", actual_s2_valid.getNickname().equals("teo"));
        // assert INVALID
        try {
            PlayerBoard actual_s2_invalid = gb_sim_2.getPlayerBoardById(5);
            fail("Testing getPlayerBoardById, simple GameMode 2 people, failed for invalid id");
        //    PlayerBoard actual_a3_invalid = gb_adv_3.getPlayerBoardById(27);
        //    fail("Testing getPlayerBoardById, advanced GameMode 3 people, failed for invalid id");
        //    PlayerBoard actual_a4_invalid = gb_adv_4.getPlayerBoardById(323);
        //    fail("Testing getPlayerBoardById, advanced GameMode 4 people, failed for invalid id");
        }
        catch(InvalidInputException e) {
            assertEquals("Invalid input provided", e.getMessage());
        }
    }
    @Test
    public void testPlayerBoardNickname() {
        // act
        // VALID
        PlayerBoard actual_s2_valid = gb_sim_2.getPlayerBoardByNickname("ale");
        //PlayerBoard actual_a3_valid = gb_adv_3.getPlayerBoardByNickname("ari");
        //PlayerBoard actual_a4_valid = gb_adv_4.getPlayerBoardByNickname("teo");


        // assert VALID
        assertTrue("Testing getPlayerBoardByNickname, simple GameMode 2 people", actual_s2_valid.getId() == 2);
        //assertTrue("Testing getPlayerBoardByNickname, advanced GameMode 3 people", actual_s2_valid.getId() == 1);
        //assertTrue("Testing getPlayerBoardByNickname, advanced GameMode 4 people", actual_s2_valid.getId() == 3);
        // assert INVALID
        try {
            PlayerBoard actual_s2_invalid = gb_sim_2.getPlayerBoardByNickname("wrong");
            fail("Testing getPlayerBoardByNickname, simple GameMode 2 people, failed for invalid nickname");
        //    PlayerBoard actual_a3_invalid = gb_adv_3.getPlayerBoardByNickname(27);
        //    fail("Testing getPlayerBoardByNickname, advanced GameMode 3 people, failed for invalid nickname");
        //    PlayerBoard actual_a4_invalid = gb_adv_4.getPlayerBoardByNickname(323);
        //    fail("Testing getPlayerBoardByNickname, advanced GameMode 4 people, failed for invalid nickname");
        }
        catch(InvalidInputException e) {
            assertEquals("Invalid input provided", e.getMessage());
        }
    }
}