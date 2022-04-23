package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InputValidationException;
import it.polimi.ingsw.Exceptions.InvalidElementException;
import it.polimi.ingsw.Exceptions.toremove.InvalidInputException;
import it.polimi.ingsw.Model.Enums.StateType;

import java.io.Serial;
import java.util.ArrayList;

import static it.polimi.ingsw.Constants.INPUT_NAME_TARGET_ISLAND;

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

    public boolean checkInput(CharacterCardInput input) throws InputValidationException {
        if(input.getTargetIsland().isEmpty()){
            throw new InvalidElementException(INPUT_NAME_TARGET_ISLAND);
        }
        if(tiles.size()==0){
            return false; //no more tiles available on card
        }
        return true;
    }

    @Override
    protected void unsafeApplyEffect(CharacterCardInput input){
        Island ti = input.getTargetIsland().get();
        for (IslandGroup ig : this.context.getIslandField().getGroups()) {
            if (ig.contains(ti)) {
                ig.addNoEntry(tiles.remove(0));
                break;
            }
        }

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
