package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Card12Test {
    Model gb = new Model(GameMode.ADVANCED, "rouge", "marianna"); // advanced mode needed for character cards
    Card12 card = new Card12(gb);

    @Test
    public void checkUse() throws Exception {
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        // creates input to let the current player interact with card
        CharacterCardInput input = new CharacterCardInput(pb);

        // adds 5 red students and 2 blue students to the dining room of every player
        for (PlayerBoard player : gb.getMutablePlayerBoards()) {
            for (int i = 0; i < 5; i++) player.unsafeAddStudentToDiningRoom(PawnColour.RED);
            for (int i = 0; i < 2; i++) player.unsafeAddStudentToDiningRoom(PawnColour.BLUE);
        }
        // activate card two times: first by selecting red colour, second by selecting blue colour
        for (PawnColour p : List.of(PawnColour.RED, PawnColour.BLUE)) {
            // select student colours (red and blue) for card to act on
            input.setTargetPawn(p);
            // act
            // should remove students of the selected colours (red first, then blue) from dining room of each player
            if (card.checkInput(input)) card.unsafeApplyEffect(input);
        }
        // checks that dining room rows have been correctly emptied
        for (PlayerBoard player : gb.getMutablePlayerBoards()) {
            assertEquals(2, player.getDiningRoomCount(PawnColour.RED));
            assertEquals(0, player.getDiningRoomCount(PawnColour.BLUE));
        }
    }
    @Test(expected = InputValidationException.class)
    public void checkEmptyInput() throws Exception {
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        // creates a wrong input which will not be filled with information
        CharacterCardInput input = new CharacterCardInput(pb);
        // act
        if (card.checkInput(input)) card.unsafeApplyEffect(input);
    }
}
