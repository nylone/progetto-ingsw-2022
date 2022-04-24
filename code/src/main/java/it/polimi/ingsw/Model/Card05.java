package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.FailedOperationException;
import it.polimi.ingsw.Exceptions.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.InputValidationException;
import it.polimi.ingsw.Exceptions.InvalidElementException;
import it.polimi.ingsw.Exceptions.toremove.InvalidInputException;
import it.polimi.ingsw.Model.Enums.StateType;

import java.io.Serial;
import java.util.ArrayList;

import static it.polimi.ingsw.Constants.*;

/*In Setup, put the 4 No Entry tiles on this card.
EFFECT: Place a No Entrytile on an Island of your choice.
The first time Mother Nature ends her movement there, put the No Entry tile back onto this card
DO NOT calculate influence on that Island, or place any Towers.
*/
public class Card05 extends StatefulEffect {
    @Serial
    private static final long serialVersionUID = 107L; // convention: 1 for model, (01 -> 99) for objects

    private final ArrayList<NoEntryTile> tiles;

    public Card05(GameBoard ctx) {
        super(5, 2, StateType.NOENTRY, ctx);
        tiles = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            tiles.add(new NoEntryTile(this));
        }
    }

    public ArrayList<Object> getState() {
        return new ArrayList<>(tiles);
    }

    public StateType getStateType() {
        return stateType;
    }

    public boolean overridableCheckInput(CharacterCardInput input) throws InputValidationException {
        if(input.getTargetIsland().isEmpty()){
            throw new InvalidInputException();
        }
        if(tiles.size()==0){
            throw new GenericInputValidationException(CONTAINER_NAME_CARD,
                    CONTAINER_NAME_CARD + "has finished its NoEntryTile");
        }
        //all tests passed
        return true;
    }

    @Override
    protected void unsafeApplyEffect(CharacterCardInput input) throws Exception{
        Island ti = input.getTargetIsland().get();
        for (IslandGroup ig : this.context.getIslandField().getGroups()) {
            if (ig.contains(ti)) {
                ig.addNoEntry(tiles.remove(0));
                return;
            }
        }
        throw new FailedOperationException(OPERATION_NAME_CARD05_APPLY_EFFECT, "Target Island was not contained in any IslandGroup");
    }

    public void tileReset(NoEntryTile tile) {
        this.tiles.add(tile);
    }

    //test-purpose only
    @Override
    public String toString() {
        return "Card05{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
