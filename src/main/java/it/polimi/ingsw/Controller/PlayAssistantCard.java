package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Exceptions.Operation.OperationException;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.Model.TurnOrder;

import java.util.List;

import static it.polimi.ingsw.Constants.INPUT_NAME_ASSISTANT_CARD;


public class PlayAssistantCard extends PlayerAction {

    private final int selectedAssistant;

    public PlayAssistantCard(int playerBoardId, int selectedAssistant) {
        super(playerBoardId);
        this.selectedAssistant = selectedAssistant - 1;
    }

    @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) throws InputValidationException {
        PlayerBoard currentPlayer = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        TurnOrder turnOrder = ctx.getMutableTurnOrder();
        if (!(this.selectedAssistant >= 0 && this.selectedAssistant <= currentPlayer.getMutableAssistantCards().size() - 1)) {
            throw new InvalidElementException(INPUT_NAME_ASSISTANT_CARD);
        }
        //assure that the player is not playing an assistant card with a priority 
        for(PlayerBoard pb : turnOrder.getCurrentTurnOrder()){
            if(turnOrder.getMutableSelectedCard(pb).isPresent() && turnOrder.getMutableSelectedCard(pb).get().getPriority() == currentPlayer.getMutableAssistantCards().get(selectedAssistant).getPriority()){
                throw new GenericInputValidationException(INPUT_NAME_ASSISTANT_CARD, INPUT_NAME_ASSISTANT_CARD + " has an already selected priority");
            }
        }
        if (currentPlayer.getMutableAssistantCards().get(selectedAssistant).getUsed()) {
            throw new GenericInputValidationException(INPUT_NAME_ASSISTANT_CARD, INPUT_NAME_ASSISTANT_CARD + "can only be used once");
        }
        if (!super.validate(history, ctx)) {
            throw new GenericInputValidationException("Action", "this action can't be executed more than once or executed by other player than the current");
        }
        return true;
    }

    public void unsafeExecute(GameBoard ctx) throws InputValidationException, OperationException {
        PlayerBoard pb = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard sa = pb.getMutableAssistantCards().get(selectedAssistant);
        ctx.getMutableTurnOrder().setSelectedCard(pb, sa);
        ctx.getMutableTurnOrder().stepToNextPlayer();
    }
}
