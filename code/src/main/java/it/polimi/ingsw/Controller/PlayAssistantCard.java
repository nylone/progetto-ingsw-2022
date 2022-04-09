package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;

public class PlayAssistantCard extends PlayerAction {

    private final int selectedAssistant;

    public PlayAssistantCard(int playerBoardId, int selectedAssistant) {
        super(playerBoardId);
        this.selectedAssistant = selectedAssistant;
    }

    public void executeAction(GameBoard ctx) {

    }
}
