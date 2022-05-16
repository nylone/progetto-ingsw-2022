package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class Card07Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card07 card = new Card07(gb);

    @Test
    public void checkUse() throws Exception {
        assertEquals(6, card.getState().size());
        assertSame(card.getStateType(), StateType.PAWNCOLOUR);

        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        CharacterCardInput input = new CharacterCardInput(pb);
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), (PawnColour) card.getState().get(0)));
        pairs.add(new Pair<>(pb.getEntranceStudents().get(1).get(), (PawnColour) card.getState().get(1)));
        Pair<PawnColour, PawnColour>[] pairsArray = new Pair[pairs.size()];
        input.setTargetPawnPairs(pairs.toArray(pairsArray));

        if (card.checkInput(input)) card.unsafeUseCard(input);

        assertTrue(card.getState().containsAll(pairs.stream().map(Pair::getFirst).toList()));
        assertTrue(pb.getEntranceStudents().containsAll(pairs.stream().map(p -> Optional.of(p.getSecond())).toList()));
    }

    @Test(expected = InputValidationException.class)
    public void checkExceptionUse() throws InvalidContainerIndexException, InputValidationException {
        PlayerBoard pb = gb.getMutablePlayerBoardByNickname("ari");
        CharacterCardInput input = new CharacterCardInput(pb);
        if (card.checkInput(input)) card.unsafeUseCard(input);
    }

    @Test(expected = InputValidationException.class)
    public void checkUse4Pawns() throws InvalidContainerIndexException, InputValidationException {
        PlayerBoard pb = gb.getMutablePlayerBoardByNickname("ari");
        CharacterCardInput input = new CharacterCardInput(pb);
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), (PawnColour) card.getState().get(0)));
        pb.removeStudentFromEntrance(0);
        pairs.add(new Pair<>(pb.getEntranceStudents().get(1).get(), (PawnColour) card.getState().get(1)));
        pb.removeStudentFromEntrance(1);
        pairs.add(new Pair<>(pb.getEntranceStudents().get(2).get(), null));
        pb.removeStudentFromEntrance(2);
        Pair<PawnColour, PawnColour>[] pairsArray = new Pair[pairs.size()];
        input.setTargetPawnPairs(pairs.toArray(pairsArray));
        if (card.checkInput(input)) card.unsafeUseCard(input);
    }
}