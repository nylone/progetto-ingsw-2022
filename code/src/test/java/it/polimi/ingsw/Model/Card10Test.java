package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.ContainerException;
import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Exceptions.Operation.OperationException;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class Card10Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "Rouge", "Rampeo");
    Card10 card10 = new Card10(gb);

    @Test
    public void checkUse() throws ContainerException, InputValidationException, OperationException {
        gb.getMutableTurnOrder().setSelectedCard(gb.getMutableTurnOrder().getMutableCurrentPlayer(), gb.getMutableTurnOrder().getMutableCurrentPlayer().getMutableAssistantCards().get(0));
        gb.getMutableTurnOrder().stepToNextPlayer();
        gb.getMutableTurnOrder().setSelectedCard(gb.getMutableTurnOrder().getMutableCurrentPlayer(), gb.getMutableTurnOrder().getMutableCurrentPlayer().getMutableAssistantCards().get(6));
        gb.getMutableTurnOrder().commitTurnOrder();

        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        pb.addStudentToDiningRoom(PawnColour.RED);
        pb.addStudentToDiningRoom(PawnColour.YELLOW);
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0), PawnColour.RED));
        pb.removeStudentFromEntrance(0);
        pb.removeStudentsFromDiningRoom(PawnColour.RED,1);
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0),  PawnColour.YELLOW));
        pb.removeStudentFromEntrance(0);
        pb.removeStudentsFromDiningRoom(PawnColour.YELLOW,1);
        Pair<PawnColour, PawnColour>[] pairsArray = new Pair[pairs.size()];
        input.setTargetPawnPairs(pairs.toArray(pairsArray));
        Arrays.stream(pairsArray).toList().forEach(p -> System.out.println(p.toString()));
        if(card10.checkInput(input)) card10.unsafeUseCard(input);
        
        assertTrue(pb.getDiningRoomCount(pairs.get(0).getFirst())==1 || pb.getDiningRoomCount(pairs.get(0).getFirst())==2); //equals 2 if students taken from entrance have the same colour
        assertTrue(pb.getDiningRoomCount(pairs.get(1).getFirst())==1 || pb.getDiningRoomCount(pairs.get(1).getFirst())==2);
        assertTrue(pb.getEntranceStudents().containsAll(pairs.stream().map(p-> p.getSecond()).toList()));
    }

    @Test(expected = InputValidationException.class)
    public void checkEmptyInput() throws Exception {
        PlayerBoard pb = gb.getMutablePlayerBoardByNickname("Rouge");
        CharacterCardInput input = new CharacterCardInput(pb);
        if(card10.checkInput(input)) card10.unsafeApplyEffect(input);
    }

    @Test(expected = InputValidationException.class)
    public void asymmetricInput() throws Exception {
        PlayerBoard pb = gb.getMutablePlayerBoardByNickname("Rouge");
        CharacterCardInput input = new CharacterCardInput(pb);
        pb.addStudentToDiningRoom(PawnColour.RED);
        pb.addStudentToDiningRoom(PawnColour.YELLOW);

        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0), PawnColour.RED));
        pb.removeStudentFromEntrance(0);
        pb.removeStudentsFromDiningRoom(PawnColour.RED,1);
        pairs.add(new Pair<>(null, PawnColour.YELLOW));
        pb.removeStudentsFromDiningRoom(PawnColour.YELLOW,1);
        if(card10.checkInput(input)) card10.unsafeApplyEffect(input);
    }
}
