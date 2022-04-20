package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.toremove.InvalidInputException;
import static org.junit.Assert.*;

import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import java.util.List;

public class Card07Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card07 card = new Card07(gb);

    @Test
    public void checkUse() {
        assertTrue(card.getState().size() == 6);
        PlayerBoard pb = gb.getPlayerBoardByNickname("ari");
        CharacterCardInput input = new CharacterCardInput(pb);
        PawnColour[][] pairs = new PawnColour[2][];
        pairs[0] = new PawnColour[]{pb.getEntranceStudents().remove(0), pb.getEntranceStudents().remove(1)};
        pairs[1] = new PawnColour[]{(PawnColour) card.getState().get(0), (PawnColour) card.getState().get(1)};
        input.setTargetPawnPairs(pairs);
        card.Use(input);
        assertTrue(card.getState().containsAll(List.of(pairs[0])));
        assertTrue(pb.getEntranceStudents().containsAll(List.of(pairs[1])));
    }

    @Test(expected = InvalidInputException.class)
    public void checkExceptionUse() {
        PlayerBoard pb = gb.getPlayerBoardByNickname("ari");
        CharacterCardInput input = new CharacterCardInput(pb);
        card.Use(input);
    }

    @Test(expected = InvalidInputException.class)
    public void checkUse4Pawns() {
        PlayerBoard pb = gb.getPlayerBoardByNickname("ari");
        CharacterCardInput input = new CharacterCardInput(pb);
        PawnColour[][] pairs = new PawnColour[2][];
        pairs[0] = new PawnColour[]{pb.getEntranceStudents().remove(0), pb.getEntranceStudents().remove(0), pb.getEntranceStudents().remove(0), pb.getEntranceStudents().remove(0)};
        pairs[1] = new PawnColour[]{(PawnColour) card.getState().get(0), (PawnColour) card.getState().get(1)};
        input.setTargetPawnPairs(pairs);
        card.Use(input);
    }
}