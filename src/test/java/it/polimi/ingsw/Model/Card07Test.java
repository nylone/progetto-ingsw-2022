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

/**
 * This test set verifies that character card 07 is able to manage, receive and give students.
 * <br>
 * It should also be able to swap a chosen number of students with ones on the player's entrance.
 */
public class Card07Test {
    Model gb = new Model(GameMode.ADVANCED, "ari", "teo"); // advanced mode needed for character cards
    Card07 card = new Card07(gb);

    /**
     * Card 07 should be able to hold 6 students and when activated to put one tile on the chosen island.
     * If the island doesn't need it anymore the card should be able to receive the tile used
     *
     * @throws Exception is thrown when an invalid card activation happens, but this test should not error out
     *                   (by definition)
     */
    @Test
    public void checkUse() throws Exception {
        assertEquals(6, card.getState().size()); // check whether the card contains 6 items initially
        assertSame(card.getStateType(), StateType.PAWNCOLOUR); // check whether the card contains students specifically

        // arrange
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        // creates input to let the current player interact with card
        CharacterCardInput input = new CharacterCardInput(pb);
        // selects the first and second students from both the card and the entrance and links them together
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), (PawnColour) card.getState().get(0)));
        pairs.add(new Pair<>(pb.getEntranceStudents().get(1).get(), (PawnColour) card.getState().get(1)));
        // input holds the selected pairs (a pair is made of one student from the entrance and one from the card)
        input.setTargetPawnPairs(pairs);

        // act
        // activates the card to swap the selected students
        if (card.checkInput(input)) card.unsafeUseCard(input);

        // assert
        // checks if the students from the entrance are on the card
        assertTrue(card.getState().containsAll(pairs.stream().map(Pair::getFirst).toList()));
        // check if the students from the card are in the entrance
        assertTrue(pb.getEntranceStudents().containsAll(pairs.stream().map(p -> Optional.of(p.getSecond())).toList()));
    }

    /**
     * An invalid player action should error out.
     * <br>
     * A pair of students has to be selected for the action to be working
     *
     * @throws Exception an invalid input has been used to activate the card
     */
    @Test(expected = InputValidationException.class)
    public void checkExceptionUse() throws InvalidContainerIndexException, InputValidationException {
        PlayerBoard pb = gb.getMutablePlayerBoardByNickname("ari");
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(pb);
        if (card.checkInput(input)) card.unsafeUseCard(input);
    }

    /**
     * An invalid player action should error out.
     * <br>
     * Maximum three student pairs can be selected for the action to be working
     *
     * @throws Exception an invalid input has been used to activate the card
     */
    @Test(expected = InputValidationException.class)
    public void checkUse4Pawns() throws InvalidContainerIndexException, InputValidationException {
        PlayerBoard pb = gb.getMutablePlayerBoardByNickname("ari");
        // creates a wrong input which will be filled with too much information
        CharacterCardInput input = new CharacterCardInput(pb);
        // todo
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), (PawnColour) card.getState().get(0)));
        pb.removeStudentFromEntrance(0);
        pairs.add(new Pair<>(pb.getEntranceStudents().get(1).get(), (PawnColour) card.getState().get(1)));
        pb.removeStudentFromEntrance(1);
        pairs.add(new Pair<>(pb.getEntranceStudents().get(2).get(), null));
        pb.removeStudentFromEntrance(2);
        input.setTargetPawnPairs(pairs);
        if (card.checkInput(input)) card.unsafeUseCard(input);
    }
}