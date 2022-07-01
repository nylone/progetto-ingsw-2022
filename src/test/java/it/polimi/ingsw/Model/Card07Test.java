package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.OptionalValue;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This test set verifies that character card 07 is able to manage, receive and give students.
 * <br>
 * It should also be able to swap a chosen number of students with ones on the player's entrance.
 */
public class Card07Test {
    final Model gb = new Model(GameMode.ADVANCED, "ari", "teo"); // advanced mode needed for character cards

    /**
     * Card 07 should be able to hold 6 students and when activated to put one tile on the chosen island.
     * If the island doesn't need it anymore the card should be able to receive the tile used
     *
     */
    @Test
    public void checkUse() {
        Card07 card = new Card07(gb);
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
        if (card.checkInput(input).isEmpty()) card.unsafeUseCard(input);

        // assert
        // checks if the students from the entrance are on the card
        assertTrue(card.getState().containsAll(pairs.stream().map(Pair::first).toList()));
        // check if the students from the card are in the entrance
        assertTrue(pb.getEntranceStudents().containsAll(pairs.stream().map(p -> OptionalValue.of(p.second())).toList()));
    }

    /**
     * An invalid player action should error out.
     * <br>
     * A pair of students has to be selected for the action to be working
     *
     * @throws Exception an invalid input has been used to activate the card
     */
    @Test(expected = InputValidationException.class)
    public void checkExceptionUse() throws Exception {
        Card07 card = new Card07(gb);
        PlayerBoard pb = gb.getMutablePlayerBoard(0);
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(pb);
        throw card.checkInput(input).get();
    }

    /**
     * An invalid player action should error out.
     * <br>
     * Maximum three student pairs can be selected for the action to be working
     *
     * @throws Exception an invalid input has been used to activate the card
     */
    @Test(expected = InputValidationException.class)
    public void checkUse4Pawns() throws Exception {
        Card07 card = new Card07(gb);
        PlayerBoard pb = gb.getMutablePlayerBoard(0);
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
        throw card.checkInput(input).get();
    }

    @Test
    public void PawnNotPresentInCard() {
        Model g = new Model(GameMode.ADVANCED, "ari", "teo");
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        Card07 card = new Card07(g);
        EnumMap<PawnColour, Integer> pawnColourIntegerEnumMap = new EnumMap<>(PawnColour.class);
        for (PawnColour p : card.getState().stream().map(o -> (PawnColour) o).toList()) {
            pawnColourIntegerEnumMap.merge(p, 1, Integer::sum);
        }
        Pair<PawnColour, PawnColour> pair = null;
        do {
            for (PawnColour p : PawnColour.values()) {
                if (!pawnColourIntegerEnumMap.containsKey(p)) {
                    input.setTargetPawn(p);
                    pair = new Pair<>(g.getMutableTurnOrder().getMutableCurrentPlayer().getEntranceStudents().get(0).get(), p);
                    break;
                }
            }
            if (pair == null) {
                g = new Model(GameMode.ADVANCED, "ari", "teo");
                input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
                pawnColourIntegerEnumMap.clear();
                card = new Card07(g);
                for (PawnColour p : card.getState().stream().map(o -> (PawnColour) o).toList()) {
                    pawnColourIntegerEnumMap.merge(p, 1, Integer::sum);
                }
            } else {
                break;
            }
        } while (true);
        input.setTargetPawnPairs(new ArrayList<>(List.of(pair)));
        InputValidationException e = card.checkInput(input).get();
        Assert.assertEquals("An error occurred while validating: Target Pawn Pairs\n" +
                "The error was: element Target Pawn Pairs was found to be invalid (eg: null, out of bounds or otherwise incorrect).", e.getMessage());
    }

    @Test
    public void PawnNotInEntrance() throws Exception {
        Model g = new Model(GameMode.ADVANCED, "ari", "teo");
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        PlayerBoard currentPlayer = g.getMutableTurnOrder().getMutableCurrentPlayer();
        Card07 card = new Card07(g);
        for (int i = 0; i < currentPlayer.getEntranceSize(); i++) {
            currentPlayer.removeStudentFromEntrance(i);
        }
        Pair<PawnColour, PawnColour> pair = new Pair<>(PawnColour.RED, (PawnColour) card.getState().get(0));
        input.setTargetPawnPairs(new ArrayList<>(List.of(pair)));
        InputValidationException e = card.checkInput(input).get();
        Assert.assertEquals("An error occurred while validating: Target Pawn Pairs\n" +
                "The error was: element Target Pawn Pairs was found to be invalid (eg: null, out of bounds or otherwise incorrect).", e.getMessage());

    }

    @Test
    public void EmptyStudentBagExceptionCardConstructor() {
        Model g = new Model(GameMode.ADVANCED, "ari", "teo");
        // creates a wrong input which will not be filled with information
        //create a new Card until student bag has 5 students or fewer
        do {
            new Card07(g);
        } while (g.getMutableStudentBag().getSize() >= 6);
        try {
            new Card07(g);
        } catch (RuntimeException e) {
            assertEquals("it.polimi.ingsw.Exceptions.Container.EmptyContainerException: An error occurred on: StudentBag\n" +
                    "The error was: StudentBag was found empty.", e.getMessage());
        }
    }

    @Test
    public void EmptyStudentBagCardUse() throws Exception {
        Model g = new Model(GameMode.ADVANCED, "ari", "teo");
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(g.getMutableTurnOrder().getMutableCurrentPlayer());
        PlayerBoard pb = g.getMutableTurnOrder().getMutableCurrentPlayer();
        // selects the first and second students from both the card and the entrance and links them together
        Card07 card = new Card07(g);
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(pb.getEntranceStudents().get(0).get(), (PawnColour) card.getState().get(0)));
        pairs.add(new Pair<>(pb.getEntranceStudents().get(1).get(), (PawnColour) card.getState().get(1)));
        input.setTargetPawnPairs(pairs);
        while (!g.getMutableStudentBag().isEmpty()) {
            g.getMutableStudentBag().extract();
        }

        InputValidationException e = card.checkInput(input).get();

        Assert.assertEquals("An error occurred while validating: Student Bag\n" +
                "The error was: is empty", e.getMessage());

    }
}