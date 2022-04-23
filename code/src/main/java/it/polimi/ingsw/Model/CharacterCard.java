package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InputValidationException;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.io.Serializable;

public abstract class CharacterCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 115L; // convention: 1 for model, (01 -> 99) for objects

    protected int id;
    protected int cost;
    protected int timeUsed;
    protected GameBoard context;

    public CharacterCard(int id, int cost, GameBoard context) {
        this.id = id;
        this.cost = cost;
        this.timeUsed = 0;
        this.context = context;
    }


    public final int getId() {
        return this.id;
    }
    public final int getCost() {
        return this.timeUsed > 0 ? this.cost + 1 : this.cost;
    }
    public final int getTimeUsed() {
        return this.timeUsed;
    }
    private void addUse() {this.timeUsed++;}

    public abstract boolean checkInput(CharacterCardInput input) throws InputValidationException;
    protected abstract void unsafeApplyEffect(CharacterCardInput input) throws Exception;
    public final void unsafeUseCard(CharacterCardInput input) {
        try { // we should never get an exception now, if we do we crash
            unsafeApplyEffect(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addUse();
    }
}
