package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameBoard;

public class EndTurnOfActionPhase extends PlayerAction {
    public EndTurnOfActionPhase(int playerBoardId) {
        super(playerBoardId);
    }

    public void unsafeExecute(GameBoard ctx) {
        // reset effects through EffectTracker
    }
}
