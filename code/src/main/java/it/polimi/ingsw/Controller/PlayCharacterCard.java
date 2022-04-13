package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.CharacterCard;
import it.polimi.ingsw.Model.CharacterCardInput;
import it.polimi.ingsw.Model.PlayerBoard;

import java.util.List;

public class PlayCharacterCard extends PlayerAction {

    private final int selectedCard;

    public PlayCharacterCard(int playerBoardId, int selectedCard) {
        super(playerBoardId);
        this.selectedCard = selectedCard;
    }

    @Override
    protected void execute(GameBoard ctx) {

    }
}
