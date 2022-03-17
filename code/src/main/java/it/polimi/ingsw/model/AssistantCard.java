package it.polimi.ingsw.model;

public class AssistantCard {
    private int cardNumber;
    private boolean used;

    public AssistantCard(int cardNumber){
        this.cardNumber = cardNumber;
        this.used = false;
    }
    public int getMaxMovement() {
        return (int) Math.ceil((double) cardNumber / 2);
    }

    public int getPriority() {
        return cardNumber;
    }
    public void useAssistantCard(){
        this.used = false;
    }
}