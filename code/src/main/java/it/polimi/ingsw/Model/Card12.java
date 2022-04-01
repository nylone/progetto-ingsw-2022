package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Card12 extends StatelessEffect{
    public Card12(GameBoard ctx) {
        super(12, 3, ctx);
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
        int pawn_to_remove = 0;
        for(PlayerBoard p : context.getPlayerBoards()){
            pawn_to_remove += Math.min(3, p.getDiningRoomCount(input.getTargetPawn().get()));
        }
        this.cost++;
    }

    //test purpose only
    @Override
    public String toString() {
        return "Card12{" +
                "id=" + id +
                ", cost=" + cost +
                ", timeUsed=" + timeUsed +
                ", context=" + context +
                '}';
    }
}
