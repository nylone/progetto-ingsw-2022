package it.polimi.ingsw.Model;

public class NoEntryTile {
    private Card05 home;

    public NoEntryTile(Card05 card){
        this.home = card;
    }

    public void goHome(){
        home.tileReset(this);
    }

}
