package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;

import java.io.Serial;
import java.io.Serializable;

import static it.polimi.ingsw.Constants.INPUT_NAME_CALLER;

public abstract class CharacterCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 115L; // convention: 1 for model, (01 -> 99) for objects

    protected int id;
    protected int cost;
    protected int timeUsed;
    protected Model context;

    public CharacterCard(int id, int cost, Model context) {
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

    /**
     * This function checks whether the correct input has been provided. It should always be called BEFORE calling an
     * unsafeApplyEffect. Keep in mind this function does not alterate the gamestate.
     *
     * @param input
     * @return can only return true as a value, only returns it when the input is correct
     * @throws InputValidationException whenever the input is invalid
     */
    public final boolean checkInput(CharacterCardInput input) throws InputValidationException {
        if (input.getCaller() == null || !input.getCaller().getNickname().equals(this.context.getMutableTurnOrder().getMutableCurrentPlayer().getNickname())) {
            throw new InvalidElementException(INPUT_NAME_CALLER);
        }
        return overridableCheckInput(input);
    }

    /**
     * This function checks whether the correct input has been provided. It is part of the checkInput function.
     * Keep in mind this function does not alterate the gamestate.
     * NOTE: checkInput(input) by default checks whether the correct player has called the card, then relays all other
     * checks to this function. So don't check the correct user in this function as it is pointless.
     *
     * @param input user's input
     * @return can only return true as a value, only returns it when the input is correct
     * @throws InputValidationException whenever the input is invalid
     */
    protected abstract boolean overridableCheckInput(CharacterCardInput input) throws InputValidationException;


    /**
     * This method must be called after checking user's input
     * Play
     *
     * @param input
     */
    public final void unsafeUseCard(CharacterCardInput input) {
        try { // we should never get an exception now, if we do we crash
            unsafeApplyEffect(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addUse();
    }

    /**
     * This method must be called after {@link #checkInput(CharacterCardInput)}
     * Execute CharacterCard's effect (NOTE: keep in mind this funcion DOES ALTERATE the gamestate)
     *
     * @param input verified user's input
     * @throws Exception not related to user's input and not recoverable
     */
    protected abstract void unsafeApplyEffect(CharacterCardInput input) throws Exception;

    private void addUse() {
        this.timeUsed++;
    }
}
