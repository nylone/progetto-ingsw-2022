package it.polimi.ingsw.Model;

import java.io.Serial;
import java.io.Serializable;

public class AssistantCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 102L; // convention: 1 for model, (01 -> 99) for objects
    private final int cardNumber;
    private boolean used;


    public AssistantCard(int cardNumber) {
        this.cardNumber = cardNumber;
        this.used = false;
    }

    public int getMaxMovement() {
        return (int) Math.ceil((double) cardNumber / 2);
    }

    public int getPriority() {
        return cardNumber;
    }

    public boolean getUsed() {
        return used;
    }

    public void setUsed() {
        this.used = true;
    }


    //test-purpose only
    @Override
    public String toString() {
        return "AssistantCard{" +
                "cardNumber=" + cardNumber +
                ", used=" + used +
                '}';
    }
}



