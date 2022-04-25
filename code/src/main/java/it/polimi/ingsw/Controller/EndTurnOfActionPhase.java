package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Model.GameBoard;

import java.util.List;

public class EndTurnOfActionPhase extends PlayerAction {
    public EndTurnOfActionPhase(int playerBoardId) {
        super(playerBoardId);
    }

    @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) throws InputValidationException {
        int MovementCount = ctx.getMutablePlayerBoards().size() == 3 ? 4 : 3;
        //turn must start by selecting an assistant card
        if(!(history.get(0).getClass() ==  PlayAssistantCard.class)){
            throw new GenericInputValidationException("History", "first PlayerAction must be playAssistantCard");
        }
        //check the amount of moved pawns
        if(
                !(history.stream()
                        .filter(playerAction -> playerAction.getClass() == MoveStudent.class)
                        .count() == MovementCount)
        ){
            throw new GenericInputValidationException("History", "There are less than "+MovementCount+" MoveStudent actions in history");
        }

        if(!super.validate(history,ctx)){
            throw new GenericInputValidationException("Action", "this action can't be executed more than once or be executed by other player than the current");
        }
        return true;
    }

    @Override
    protected void unsafeExecute(GameBoard ctx) {
        // reset effects through EffectTracker
        ctx.getMutableEffects().reset();
    }




}
