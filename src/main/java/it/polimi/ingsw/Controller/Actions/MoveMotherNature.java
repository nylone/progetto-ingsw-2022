package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

import java.io.Serial;
import java.util.List;

public class MoveMotherNature extends PlayerAction {
    @Serial
    private static final long serialVersionUID = 204L; // convention: 2 for controller, (01 -> 99) for objects

    private final int distanceToMove;

    public MoveMotherNature(int playerBoardId, int distanceToMove) {
        super(playerBoardId);
        this.distanceToMove = distanceToMove;
    }

    @Override
    public boolean validate(List<PlayerAction> history, GameBoard ctx) throws InputValidationException {
        PlayerBoard currentPlayer = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        int maxCount = ctx.getMutablePlayerBoards().size() == 3 ? 4 : 3;
        Optional<AssistantCard> optionalAssistantCard = ctx.getMutableTurnOrder()
                .getMutableSelectedCard(currentPlayer);

        if (!super.validate(history, ctx)) {
            throw new GenericInputValidationException("Action", "this action can't be executed more than once or be executed by other player than the current");
        }
        if (
                (!(history.stream().filter(playerAction -> playerAction.getClass() == MoveStudent.class).count() == maxCount))
        ) {
            throw new GenericInputValidationException("History", "MotherNature can't be moved before having placed all " + maxCount + " pawns");
        }
        if (!(history.get(history.size() - 1).getClass() == MoveStudent.class || (history.get(history.size() - 1).getClass() == PlayCharacterCard.class))) {
            throw new GenericInputValidationException("History", "This action can only be executed after a MoveStudent action or PlayCharacterCard action");
        }
        int maxMovement = optionalAssistantCard.get().getMaxMovement();
        if (!(distanceToMove >= 1 &&
                distanceToMove <= (ctx.getMutableEffects().isMotherNatureMovementIncreased() ?
                        maxMovement + 2 : maxMovement)
        )) {
            throw new InvalidElementException("DistanceToMove");
        }
        return true;
    }

    @Override
    public void unsafeExecute(GameBoard ctx) {
        ctx.moveAndActMotherNature(distanceToMove);
    }

}
