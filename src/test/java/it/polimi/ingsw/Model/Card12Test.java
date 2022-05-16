package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Card12Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "rouge", "marianna");
    Card12 card = new Card12(gb);

    @Test
    public void checkUse() throws Exception {
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        CharacterCardInput input = new CharacterCardInput(pb);

        for (int i = 0; i <= 4; i++) pb.addStudentToDiningRoom(PawnColour.RED);
        input.setTargetPawn(PawnColour.RED);
        if (card.checkInput(input)) card.unsafeApplyEffect(input);

        assertEquals(2, pb.getDiningRoomCount(PawnColour.RED));
    }

    @Test(expected = InputValidationException.class)
    public void checkEmptyInput() throws Exception {
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        CharacterCardInput input = new CharacterCardInput(pb);
        if (card.checkInput(input)) card.unsafeApplyEffect(input);
    }
}
