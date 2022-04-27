package it.polimi.ingsw.Model;


import it.polimi.ingsw.Controller.GameHandler;
import it.polimi.ingsw.Controller.MoveMotherNature;
import it.polimi.ingsw.Controller.PlayAssistantCard;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class Card04Test {

    @Test
    @Ignore
    public void mnCanMove2AdditionalPositions() throws Exception {
        // arrange & act
        GameHandler gh = new GameHandler(GameMode.ADVANCED, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        Card04 card = new Card04(gameBoard);
        IslandGroup initialMnPosition = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        int chosenCard = new Random().nextInt(9);
        PlayAssistantCard selectCard = new PlayAssistantCard(currentPlayer.getId(),
                chosenCard);
        CharacterCardInput input = new CharacterCardInput(currentPlayer);
        gh.executeAction(selectCard);
        int additionalMovement = new Random().nextInt(2);
        card.unsafeApplyEffect(new CharacterCardInput(currentPlayer));
        int movement = gameBoard.getMutableTurnOrder().getMutableSelectedCard(currentPlayer).get().getMaxMovement()
                + additionalMovement;
        MoveMotherNature movingAction = new MoveMotherNature(currentPlayer.getId(),
                movement);
        gh.executeAction(movingAction);
        // assert
        IslandGroup actual = gameBoard.getMutableIslandField().getMutableMotherNaturePosition();
        assertEquals(Utils.modularSelection(initialMnPosition,
                gameBoard.getMutableIslandField().getMutableGroups(), movement), actual);
    }
}