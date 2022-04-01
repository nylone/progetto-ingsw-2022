package it.polimi.ingsw.Model;

public class Card08 extends StatelessEffect{
    public Card08(GameBoard ctx) {
        super(8, 2, ctx);
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
        context.setIncreasedInfluenceFlag(true);
        this.cost++;
    }

    //test purpose only
    @Override
    public String toString() {
        return "Card08{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
