package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.ContainerException;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class Card10Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "Rouge", "Rampeo");
    Card10 card10 = new Card10(gb);

    @Test
    public void checkUse() throws ContainerException, InputValidationException {
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        pb.addStudentToDiningRoom(PawnColour.RED);
        pb.addStudentToDiningRoom(PawnColour.YELLOW);
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), PawnColour.RED));
        pairs.add(new Pair<>(pb.getEntranceStudents().get(1).get(), PawnColour.YELLOW));
        Pair<PawnColour, PawnColour>[] pairsArray = new Pair[pairs.size()];
        input.setTargetPawnPairs(pairs.toArray(pairsArray));
        if (card10.checkInput(input)) card10.unsafeUseCard(input);

        assertTrue(pb.getDiningRoomCount(pairs.get(0).getFirst()) == 1 || pb.getDiningRoomCount(pairs.get(0).getFirst()) == 2); //equals 2 if students taken from entrance have the same colour
        assertTrue(pb.getDiningRoomCount(pairs.get(1).getFirst()) == 1 || pb.getDiningRoomCount(pairs.get(1).getFirst()) == 2);
        assertTrue(pb.getEntranceStudents().containsAll(pairs.stream().map(p -> Optional.of(p.getSecond())).toList()));
    }

    @Test(expected = InputValidationException.class)
    public void checkEmptyInput() throws Exception {
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        CharacterCardInput input = new CharacterCardInput(pb);
        if (card10.checkInput(input)) card10.unsafeApplyEffect(input);
    }

    @Test(expected = InputValidationException.class)
    public void asymmetricInput() throws Exception {
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        CharacterCardInput input = new CharacterCardInput(pb);
        pb.addStudentToDiningRoom(PawnColour.RED);
        pb.addStudentToDiningRoom(PawnColour.YELLOW);

        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), PawnColour.RED));
        pairs.add(new Pair<>(null, PawnColour.YELLOW));
        if (card10.checkInput(input)) card10.unsafeApplyEffect(input);
    }

    @Test(expected = InputValidationException.class)
    public void checkInvalidEntranceSize() throws Exception {
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        pb.addStudentToDiningRoom(PawnColour.RED);
        pb.addStudentToDiningRoom(PawnColour.YELLOW);
        for (int i = 1; i < pb.getEntranceSize(); i++) {
            pb.removeStudentFromEntrance(i);
        }
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), PawnColour.RED));
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), PawnColour.YELLOW));
        Pair<PawnColour, PawnColour>[] pairsArray = new Pair[pairs.size()];
        input.setTargetPawnPairs(pairs.toArray(pairsArray));
        if (card10.checkInput(input)) card10.unsafeUseCard(input);
    }

    @Test(expected = GenericInputValidationException.class)
    public void checkInvalidDiningRoomSize() throws Exception {
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        pb.addStudentToDiningRoom(PawnColour.RED);
        pb.addStudentToDiningRoom(PawnColour.BLUE);
        pb.removeStudentFromEntrance(0);
        pb.addStudentsToEntrance(List.of(PawnColour.RED));
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), PawnColour.BLUE));
        for (int i = 0; i < 9; i++)
            pb.addStudentToDiningRoom(pb.getEntranceStudents().get(0).get());
        Pair<PawnColour, PawnColour>[] pairsArray = new Pair[pairs.size()];
        input.setTargetPawnPairs(pairs.toArray(pairsArray));
        if (card10.checkInput(input)) card10.unsafeUseCard(input);
    }
}
