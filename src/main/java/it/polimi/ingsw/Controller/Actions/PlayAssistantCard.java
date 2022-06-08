package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Exceptions.Operation.OperationException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.Model.TurnOrder;

import java.io.Serial;
import java.util.List;

import static it.polimi.ingsw.Constants.INPUT_NAME_ASSISTANT_CARD;


public class PlayAssistantCard extends PlayerAction {
    @Serial
    private static final long serialVersionUID = 206L; // convention: 2 for controller, (01 -> 99) for objects

    private final int selectedAssistant;

    public PlayAssistantCard(int playerBoardId, int selectedAssistant) {
        super(playerBoardId, true);
        this.selectedAssistant = selectedAssistant - 1;
    }

    @Override
    protected Optional<InputValidationException> customValidation(List<PlayerAction> history, Model ctx) {
        PlayerBoard currentPlayer = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        TurnOrder turnOrder = ctx.getMutableTurnOrder();
        if (!(this.selectedAssistant >= 0 && this.selectedAssistant <= currentPlayer.getMutableAssistantCards().size() - 1)) {
            return Optional.of(new InvalidElementException(INPUT_NAME_ASSISTANT_CARD));
        }
        //assure that the player is not playing an assistant card with a priority 
        for (PlayerBoard pb : turnOrder.getCurrentTurnOrder()) {
            if (turnOrder.getMutableSelectedCard(pb).isPresent() && turnOrder.getMutableSelectedCard(pb).get().getPriority() == currentPlayer.getMutableAssistantCards().get(selectedAssistant).getPriority()) {
                return Optional.of(new GenericInputValidationException(INPUT_NAME_ASSISTANT_CARD, INPUT_NAME_ASSISTANT_CARD + " has an already selected priority"));
            }
        }
        if (currentPlayer.getMutableAssistantCards().get(selectedAssistant).getUsed()) {
            return Optional.of(new GenericInputValidationException(INPUT_NAME_ASSISTANT_CARD, INPUT_NAME_ASSISTANT_CARD + "can only be used once"));
        }
        return Optional.empty();
    }

    public void unsafeExecute(Model ctx) throws InputValidationException, OperationException {
        PlayerBoard pb = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard sa = pb.getMutableAssistantCards().get(selectedAssistant);
        ctx.getMutableTurnOrder().setSelectedCard(pb, sa);
        ctx.getMutableTurnOrder().stepToNextPlayer();
    }
}
