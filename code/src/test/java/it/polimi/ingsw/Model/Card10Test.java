package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.ContainerException;
import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Card10Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "Rouge", "Rampeo");
    Card10 card10 = new Card10(gb);

    @Test
    public void checkUse() throws ContainerException, InvalidElementException {
        PlayerBoard pb = gb.getMutablePlayerBoardByNickname("Rouge");
        CharacterCardInput input = new CharacterCardInput(pb);
        pb.addStudentToDiningRoom(PawnColour.RED);
        pb.addStudentToDiningRoom(PawnColour.YELLOW);
        PawnColour[][] pairs = new PawnColour[2][];
        pairs[0] = new PawnColour[]{pb.getEntranceStudents().remove(0), pb.getEntranceStudents().remove(0)};
        pairs[1] = new PawnColour[]{PawnColour.RED, PawnColour.YELLOW};
        pb.removeStudentsFromDiningRoom(PawnColour.RED,1);
        pb.removeStudentsFromDiningRoom(PawnColour.YELLOW,1);
        input.setTargetPawnPairs(pairs);
        card10.Use(input);
        assertTrue(pb.getDiningRoomCount(pairs[0][0])==1 || pb.getDiningRoomCount(pairs[0][0])==2); //equals 2 if students taken from entrance have the same colour
        assertTrue(pb.getDiningRoomCount(pairs[0][1])==1 || pb.getDiningRoomCount(pairs[0][1])==2);
        assertTrue(pb.getEntranceStudents().contains(pairs[1][0]));
        assertTrue(pb.getEntranceStudents().contains(pairs[1][1]));
    }

    @Test(expected = FullContainerException.class)
    public void checkEmptyInput() throws Exception {
        PlayerBoard pb = gb.getMutablePlayerBoardByNickname("Rouge");
        CharacterCardInput input = new CharacterCardInput(pb);
        card10.unsafeApplyEffect(input);
    }

    @Test(expected = FullContainerException.class)
    public void asymmetricInput() throws Exception {
        PlayerBoard pb = gb.getMutablePlayerBoardByNickname("Rouge");
        CharacterCardInput input = new CharacterCardInput(pb);
        pb.addStudentToDiningRoom(PawnColour.RED);
        pb.addStudentToDiningRoom(PawnColour.YELLOW);
        PawnColour[][] pairs = new PawnColour[2][];
        pairs[0] = new PawnColour[]{pb.getEntranceStudents().remove(0)};
        pairs[1] = new PawnColour[]{PawnColour.RED, PawnColour.YELLOW};
        card10.unsafeApplyEffect(input);
    }
}
