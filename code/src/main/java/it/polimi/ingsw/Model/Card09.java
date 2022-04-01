package it.polimi.ingsw.Model;

public class Card09 extends StatelessEffect{
    public Card09(GameBoard ctx) {
        super(9, 3, ctx);
    }

    public int getId() {
        return this.id;
    }

    public int getCost() {
        return this.cost;
    }

    public int getTimeUsed() {
        return this.timeUsed;
    }

    public void Use(CharacterCardInput input) {
        //todo
        this.context.setDenyPawnColourInfluence(input.getTargetPawn());
        this.cost++;
    }

    //test purpose only
    @Override
    public String toString() {
        return "Card09{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
