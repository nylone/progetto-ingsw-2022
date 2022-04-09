package it.polimi.ingsw.Model;
import com.sun.source.tree.AssertTree;
import it.polimi.ingsw.Exceptions.EmptyDiningRoomException;
import it.polimi.ingsw.Exceptions.FullDiningRoomException;
import it.polimi.ingsw.Exceptions.InvalidInputException;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Card10Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "Rouge", "Rampeo");
    Card10 card10 = new Card10(gb);

    @Test
    public void checkUse() throws FullDiningRoomException, EmptyDiningRoomException {
        PlayerBoard pb = gb.getPlayerBoardByNickname("Rouge");
        CharacterCardInput input = new CharacterCardInput(pb);
        pb.addStudentToDiningRoom(PawnColour.RED);
        pb.addStudentToDiningRoom(PawnColour.YELLOW);
        PawnColour[][] pairs = new PawnColour[2][];
        pairs[0] = new PawnColour[]{pb.getEntranceStudents().remove(0), pb.getEntranceStudents().remove(0)};
        pairs[1] = new PawnColour[]{PawnColour.RED, PawnColour.YELLOW};
        pb.removeStudentFromDiningRoom(PawnColour.RED,1);
        pb.removeStudentFromDiningRoom(PawnColour.YELLOW,1);
        input.setTargetPawnPairs(pairs);
        card10.Use(input);
        assertTrue(pb.getDiningRoomCount(pairs[0][0])==1 || pb.getDiningRoomCount(pairs[0][0])==2); //equals 2 if students taken from entrance have the same colour
        assertTrue(pb.getDiningRoomCount(pairs[0][1])==1 || pb.getDiningRoomCount(pairs[0][1])==2);
        assertTrue(pb.getEntranceStudents().contains(pairs[1][0]));
        assertTrue(pb.getEntranceStudents().contains(pairs[1][1]));
    }

    @Test(expected = InvalidInputException.class)
    public void checkEmptyInput(){
        PlayerBoard pb = gb.getPlayerBoardByNickname("Rouge");
        CharacterCardInput input = new CharacterCardInput(pb);
        card10.Use(input);
    }
    @Test(expected = InvalidInputException.class)
    public void asymettricInput() throws FullDiningRoomException {
        PlayerBoard pb = gb.getPlayerBoardByNickname("Rouge");
        CharacterCardInput input = new CharacterCardInput(pb);
        pb.addStudentToDiningRoom(PawnColour.RED);
        pb.addStudentToDiningRoom(PawnColour.YELLOW);
        PawnColour[][] pairs = new PawnColour[2][];
        pairs[0] = new PawnColour[]{pb.getEntranceStudents().remove(0)};
        pairs[1] = new PawnColour[]{PawnColour.RED, PawnColour.YELLOW};
        card10.Use(input);
    }
}
