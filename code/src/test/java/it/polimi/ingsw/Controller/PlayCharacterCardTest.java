package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.Optional;
import org.junit.Ignore;
import org.junit.Test;

public class PlayCharacterCardTest {

    @Test @Ignore
    public void checkUse(){
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        //PlayCharacterCard playAction = new PlayCharacterCard(player.getId(), 0);

    }

    @Test(expected = InputValidationException.class)
    public void CharacterCardIndexOutOfBound() throws Exception{
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getContext();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayCharacterCard playAction = new PlayCharacterCard(player.getId(),5, Optional.empty(), Optional.empty(), Optional.empty());
        playAction.safeExecute(gh.getHistory(), gameBoard);
    }
}