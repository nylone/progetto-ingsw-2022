package it.polimi.ingsw.Model;

import java.io.Serial;
import java.io.Serializable;

/**
 * This class represents one of the 10 player owned assistant cards
 */
public class AssistantCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 102L; // convention: 1 for model, (01 -> 99) for objects
    private final int priority;
    private boolean used;

    /**
     * Constructs an Assistant card based on its priority.
     * @param priority the priority of the card
     */
    public AssistantCard(int priority) {
        this.priority = priority;
        this.used = false;
    }

    /**
     * Every card has a maximum value for the movement to use in {@link it.polimi.ingsw.Controller.Actions.MoveMotherNature}
     * @return an integer value representing max mother nature movement
     */
    public int getMaxMovement() {
        return (int) Math.ceil((double) priority / 2);
    }

    /**
     * Returns the priority of turn linked to the use of the card.
     * @return an integer value representing priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * If a card is used in a turn by the player, this flag must be set to true.
     * @return the used flag of the card
     */
    public boolean getUsed() {
        return used;
    }

    /**
     * If a card is used in a turn by the player, this flag must be set to true. <br>
     * Use this function to set the value to true. Keep in mind there is no way to revert this flag once set.
     */
    public void setUsed() {
        this.used = true;
    }

    /*//test-purpose only
    @Override
    public String toString() {
        return "AssistantCard{" +
                "cardNumber=" + priority +
                ", used=" + used +
                '}';
    }
    // */
}



