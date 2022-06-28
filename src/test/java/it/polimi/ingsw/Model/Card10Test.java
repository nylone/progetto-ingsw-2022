package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.ContainerException;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Misc.SerializableOptional;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * This test set verifies that character card 10 is able to swap up to 2 students from entrance to dining room and
 * vice-versa; the set also covers the edge cases related to invalid actions from the players.
 */
public class Card10Test {
    Model gb = new Model(GameMode.ADVANCED, "Rouge", "Rampeo"); // advanced mode needed for character cards
    Card10 card10 = new Card10(gb);

    /**
     * Card 10 should be able to move students from entrance to dining room and vice-versa
     *
     * @throws ContainerException       is thrown when you try to add a student to a full dining room,
     *                                  but this test should not error out (by definition)
     * @throws InputValidationException is thrown when an invalid card activation happens, but this test
     *                                  should not error out (by definition)
     */
    @Test
    public void checkUse() throws ContainerException, InputValidationException {
        // arrange
        // creates input to let the current player interact with card
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        pb.unsafeAddStudentToDiningRoom(PawnColour.RED);
        pb.unsafeAddStudentToDiningRoom(PawnColour.YELLOW);
        // selects the first and second students from both the dining room and the entrance and links them together
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), PawnColour.RED));
        pairs.add(new Pair<>(pb.getEntranceStudents().get(1).get(), PawnColour.YELLOW));
        // input holds the selected pairs (a pair is made of one student from the entrance and one from the dining room)
        input.setTargetPawnPairs(pairs);

        // act
        // activates the card to swap the selected students
        if (card10.checkInput(input)) card10.unsafeUseCard(input);

        // assert
        // check that students have been added to the dining room
        // if chosen students from entrance have same colour -> one row in the dining room should have 2 students
        // if chosen students from entrance have different colour -> two row in the dining room should have 1 student
        assertTrue(pb.getDiningRoomCount(pairs.get(0).getFirst()) == 1 || pb.getDiningRoomCount(pairs.get(0).getFirst()) == 2);
        assertTrue(pb.getDiningRoomCount(pairs.get(1).getFirst()) == 1 || pb.getDiningRoomCount(pairs.get(1).getFirst()) == 2);
        // check that both students from the dining room have been added to the entrance
        assertTrue(pb.getEntranceStudents().containsAll(pairs.stream().map(p -> SerializableOptional.of(p.getSecond())).toList()));
    }

    /**
     * An invalid player action should error out.
     * <br>
     * A pair of students has to be selected for the action to be working
     *
     * @throws Exception an invalid input has been used to activate the card
     */
    @Test(expected = InputValidationException.class)
    public void checkEmptyInput() throws Exception {
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(pb);
        if (card10.checkInput(input)) card10.unsafeApplyEffect(input);
    }

    // todo
    @Test(expected = InputValidationException.class)
    public void asymmetricInput() throws Exception {
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        CharacterCardInput input = new CharacterCardInput(pb);
        pb.unsafeAddStudentToDiningRoom(PawnColour.RED);
        pb.unsafeAddStudentToDiningRoom(PawnColour.YELLOW);

        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), PawnColour.RED));
        pairs.add(new Pair<>(null, PawnColour.YELLOW));
        if (card10.checkInput(input)) card10.unsafeApplyEffect(input);
    }

    /**
     * An invalid player action should error out.
     * <br>
     * Check that same student from entrance cannot be swapped twice
     *
     * @throws Exception an invalid input has been used to activate the card
     */
    @Test(expected = InputValidationException.class)
    public void checkInvalidEntranceSize() throws Exception {
        // arrange
        // creates a wrong input which will be filled with too much information
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        pb.unsafeAddStudentToDiningRoom(PawnColour.RED);
        pb.unsafeAddStudentToDiningRoom(PawnColour.YELLOW);
        // removes every student from entrance except the first
        for (int i = 1; i < pb.getEntranceSize(); i++) {
            pb.removeStudentFromEntrance(i);
        }
        // selects the first and second students from both the dining room and the entrance and links them together
        // both times select the only student in the entrance
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), PawnColour.RED));
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), PawnColour.YELLOW));
        // input holds the selected pairs (a pair is made of one student from the entrance and one from the dining room)
        input.setTargetPawnPairs(pairs);

        // act
        if (card10.checkInput(input)) card10.unsafeUseCard(input);

    }

    /**
     * If the selected student from the entrance has the same colour of a full dining room's row
     * you cannot swap this student
     *
     * @throws Exception an invalid input has been used to activate the card
     */
    @Test(expected = GenericInputValidationException.class)
    public void checkInvalidDiningRoomSize() throws Exception {
        // arrange
        // creates a wrong input which will be filled with student that cannot be added to dining room
        CharacterCardInput input = new CharacterCardInput(gb.getMutableTurnOrder().getMutableCurrentPlayer());
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        // forces the first student in the entrance to be red
        pb.removeStudentFromEntrance(0);
        pb.addStudentsToEntrance(List.of(PawnColour.RED));

        // completely fill the red row in dining room
        for (int i = 0; i < 10; i++)
            pb.unsafeAddStudentToDiningRoom(pb.getEntranceStudents().get(0).get());
        // add one blue student to dining room to be swapped with the red one in the entrance
        pb.unsafeAddStudentToDiningRoom(PawnColour.BLUE);

        // selects red student from entrance and blue colour (representing student from dining room)
        // and links them together
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), PawnColour.BLUE));
        // input holds the selected pairs (a pair is made of one student from the entrance and one from the dining room)
        input.setTargetPawnPairs(pairs);

        // act
        if (card10.checkInput(input)) card10.unsafeUseCard(input);
    }
}
