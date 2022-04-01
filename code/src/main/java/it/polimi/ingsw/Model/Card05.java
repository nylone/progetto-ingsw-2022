package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Card05 extends StatefulEffect{
    private ArrayList<NoEntryTile> tiles;

    public Card05(GameBoard ctx){
        super(5,2,StateType.NOENTRY,ctx);
        tiles = new ArrayList<>(4);
        for(int i=0; i<4; i++){
            tiles.add(new NoEntryTile(this));
        }
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public int getTimeUsed() {
        return timeUsed;
    }

    public ArrayList<Object> getState() {
        return new ArrayList<>(tiles);
    }

    public StateType getStateType() {
        return null;
    }

    public void Use(CharacterCardInput input) {
        //todo
        Optional<IslandGroup> islandGroup = Optional.empty();
        for(IslandGroup ig : this.context.getIslandField().getGroups()){
            islandGroup = ig.find(input.getTargetIsland().get());
            if(islandGroup.isPresent()) break;
        }
        islandGroup.get().setNoEntry(Optional.of(tiles.remove(0)));
        //todo
    }


    public void tileReset(NoEntryTile tile){
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
