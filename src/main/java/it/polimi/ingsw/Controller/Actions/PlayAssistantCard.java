package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Exceptions.Operation.OperationException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GamePhase;

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

    /**
     * {@inheritDoc}
     * <ul>
     *     <li>The {@link GamePhase} must be {@link GamePhase#SETUP}</li>
     *     <li>The selected assistant card must be within bounds (always greater or equal to 0, always lesser or equal to the size of
     *     the player's assistants deck</li>
     *     <li>The player must play a card that has not been chosen by other players before (unless there are no other cards left to choose from)</li>
     *     <li>The selected {@link AssistantCard} can only be used once by the player</li>
     * </ul>
     *
     * @param history the controller stores a {@link List} of previous {@link PlayerAction}s related to the player taking
     *                the current turn (at every new turn, the history is cleared).
     *                Some actions may use this {@link List} to check for duplicates.
     * @param ctx     a reference to {@link Model}. Some actions may use this reference to check for consistency between what
     *                the actions declares and what the Model offers.
     * @return An empty {@link Optional} in case of a successful validation. Otherwise the returned {@link Optional}
     * contains the related {@link InputValidationException}
     */
    @Override
    protected Optional<InputValidationException> customValidation(List<PlayerAction> history, Model ctx) {
        PlayerBoard currentPlayer = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        TurnOrder turnOrder = ctx.getMutableTurnOrder();
        if (ctx.getMutableTurnOrder().getGamePhase() != GamePhase.SETUP) {
            return Optional.of(new GenericInputValidationException(INPUT_NAME_ASSISTANT_CARD, INPUT_NAME_ASSISTANT_CARD + "may only be used during the setup phase"));
        }
        if (!(this.selectedAssistant >= 0 && this.selectedAssistant <= currentPlayer.getMutableAssistantCards().size() - 1)) {
            return Optional.of(new InvalidElementException(INPUT_NAME_ASSISTANT_CARD));
        }
        AssistantCard selectedCard = currentPlayer.getMutableAssistantCards().get(selectedAssistant);
        if (selectedCard.getUsed()) {
            return Optional.of(new GenericInputValidationException(INPUT_NAME_ASSISTANT_CARD, INPUT_NAME_ASSISTANT_CARD + "can only be used once"));
        }
        if (ctx.getMutableTurnOrder().isAlreadyInSelection(selectedCard) && turnOrder.canPlayUniqueCard(currentPlayer)) {
            return Optional.of(new GenericInputValidationException(INPUT_NAME_ASSISTANT_CARD, INPUT_NAME_ASSISTANT_CARD + " has already been selected by another player"));
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
