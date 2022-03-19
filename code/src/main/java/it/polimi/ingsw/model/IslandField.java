package it.polimi.ingsw.model;

import java.util.ArrayList;

public class IslandField {
    //GameBoard context;
    private ArrayList<IslandGroup> groups;
    private ArrayList<Island> islands;
    private IslandGroup motherNaturePosition;

    //Todo dopo aver ottenuto la scelta dell'isola su cui piazzare madre natura bisogna riempire le isole rimanenti con gli studenti (eccetto l'isola opposta)
    public IslandField(){
        groups = new ArrayList<>();
        islands = new ArrayList<>();
        for(int i=1; i<=12; i++){
            Island island = new Island(i);
            IslandGroup islandGroup = new IslandGroup(island);
            islands.add(island);
            groups.add(islandGroup);
        }
        motherNaturePosition = null;
    }

    public ArrayList<IslandGroup> getGroups() {
        return groups;
    }

    public ArrayList<Island> getIslands() {
        return islands;
    }

    public IslandGroup getMotherNaturePosition() {
        return motherNaturePosition;
    }

    public void setMotherNaturePosition(IslandGroup motherNaturePosition) {
        this.motherNaturePosition = motherNaturePosition;
    }

    public void moveMotherNature(int moves){
        motherNaturePosition = groups.get((groups.indexOf(motherNaturePosition)+moves)%groups.size());
    }

}
