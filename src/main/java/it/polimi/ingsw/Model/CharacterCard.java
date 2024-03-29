package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.OptionalValue;

import java.io.Serial;
import java.io.Serializable;

public abstract class CharacterCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 115L; // convention: 1 for model, (01 -> 99) for objects

    protected final int id;
    protected final int cost;
    protected final Model context;
    protected int timeUsed;

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
     * @param input user's input object
     * @return a non empty {@link OptionalValue} containing a validation error. Or an empty one when the input is correct
     */
    public final OptionalValue<InputValidationException> checkInput(CharacterCardInput input) {
        if (input.getCaller() == null || !input.getCaller().getNickname().equals(this.context.getMutableTurnOrder().getMutableCurrentPlayer().getNickname())) {

            return OptionalValue.of(new InvalidElementException("Card Caller"));
        }
        return overridableCheckInput(input);
    }

    /**
     * This function checks whether the correct input has been provided. It is part of the checkInput function.
     * Keep in mind this function does not alterate the gamestate.
     * NOTE: checkInput(input) by default checks whether the correct player has called the card, then relays all other
     * checks to this function. So don't check the correct user in this function as it is pointless.
     *
     * @param input user's input object
     * @return a non empty {@link OptionalValue} containing a validation error. Or an empty one when the input is correct
     */
    protected abstract OptionalValue<InputValidationException> overridableCheckInput(CharacterCardInput input);


    /**
     * This method must be called after checking user's input
     * Play
     *
     * @param input a {@link CharacterCardInput} object, the same used during validation
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
