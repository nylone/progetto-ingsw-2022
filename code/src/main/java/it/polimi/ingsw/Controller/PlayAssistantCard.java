package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

import java.util.List;

public class PlayAssistantCard extends PlayerAction {

    private final int selectedAssistant;

    public PlayAssistantCard(int playerBoardId, int selectedAssistant) {
        super(playerBoardId);
        this.selectedAssistant = selectedAssistant - 1;
    }

    public void unsafeExecute(GameBoard ctx) {
        PlayerBoard pb = ctx.getTurnOrder().getCurrentPlayer();
        AssistantCard sa = pb.getAssistantCards()[selectedAssistant];
        ctx.getTurnOrder().setSelectedCard(pb, sa);
    }

    @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) {
        PlayerBoard currentPlayer = ctx.getTurnOrder().getCurrentPlayer();

        return super.validate(history, ctx) &&
                this.selectedAssistant >= 0 && this.selectedAssistant <= currentPlayer.getAssistantCards().length - 1 &&
                !currentPlayer.getAssistantCards()[selectedAssistant].getUsed(); //true if not used/ false if the card has been used

    }
}
