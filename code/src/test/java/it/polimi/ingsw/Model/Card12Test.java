package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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

        assertTrue(pb.getDiningRoomCount(PawnColour.RED) == 2);
    }

    @Test(expected = InputValidationException.class)
    public void checkEmptyInput() throws Exception {
        gb.getMutableTurnOrder().setSelectedCard(gb.getMutableTurnOrder().getMutableCurrentPlayer(), gb.getMutableTurnOrder().getMutableCurrentPlayer().getMutableAssistantCards().get(0));
        gb.getMutableTurnOrder().stepToNextPlayer();
        gb.getMutableTurnOrder().setSelectedCard(gb.getMutableTurnOrder().getMutableCurrentPlayer(), gb.getMutableTurnOrder().getMutableCurrentPlayer().getMutableAssistantCards().get(6));
        gb.getMutableTurnOrder().commitTurnOrder();
        PlayerBoard pb = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        CharacterCardInput input = new CharacterCardInput(pb);
        if (card.checkInput(input)) card.unsafeApplyEffect(input);
    }
}
