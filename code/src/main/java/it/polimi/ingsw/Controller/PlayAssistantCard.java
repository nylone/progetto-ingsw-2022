package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.PlayerBoard;

import java.util.List;
import java.util.Optional;

public class PlayAssistantCard extends PlayerAction {

    private final int selectedAssistant;

    public PlayAssistantCard(int playerBoardId, int selectedAssistant) {
        super(playerBoardId);
        this.selectedAssistant = selectedAssistant;
    }


    public void execute(GameBoard ctx) {
        PlayerBoard pb = ctx.getTurnOrder().getCurrent();
        pb.getAssistantCards()[selectedAssistant].use();

    }

    @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) {
        PlayerBoard currentPlayer = ctx.getTurnOrder().getCurrent();

        return super.validate(history,ctx) &&
                this.selectedAssistant>=0 && this.selectedAssistant<=currentPlayer.getAssistantCards().length-1 &&
               !currentPlayer.getAssistantCards()[selectedAssistant].getUsed(); //true if not used/ false if the card has been used

    }
}
