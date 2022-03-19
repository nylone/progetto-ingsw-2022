package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class IslandField {
    private GameBoard context;
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

    public void moveMotherNature(int moves){
        motherNaturePosition = groups.get((groups.indexOf(motherNaturePosition)+moves)%groups.size());
    }
    public void joinGroups(){
        //check if neighbours islands contain same tower-colour
        AtomicInteger actualPosition = new AtomicInteger(groups.indexOf(motherNaturePosition));
        int previous_index = actualPosition.accumulateAndGet(1, (index,dec) -> (--index < 0 ? groups.size() : index));
        int next_index = actualPosition.accumulateAndGet(1, (index,acc) -> (++index > groups.size() ? 0 : index));
        Optional<TowerColour> actualTowerColour = motherNaturePosition.getIslands().get(0).getTower(); //get the towerColour of actual IslandGroup
        Optional<TowerColour> previousTowerColour = groups.get(previous_index).getIslands().get(0).getTower(); //get the towerColour of previous IslandGroup
        Optional<TowerColour> nextTowerColour = groups.get(next_index).getIslands().get(0).getTower(); //get the towerColour of next islandGroup
        if(actualTowerColour.equals(nextTowerColour) && actualTowerColour.equals(previousTowerColour)){ //merge three different islands
            ArrayList<IslandGroup> toRemove = new ArrayList<>(); //support list
            toRemove.add(motherNaturePosition);
            toRemove.add(groups.get(previous_index));
            toRemove.add(groups.get(next_index));
            IslandGroup threeMerge = new IslandGroup(motherNaturePosition, groups.get(previous_index), groups.get(next_index));
            motherNaturePosition = threeMerge;
            groups.add(previous_index,threeMerge); //set the new GroupIsland at the lowest index of the three groupIslands
            groups.removeAll(toRemove); //remove old GroupIslands

        }else if(actualTowerColour.equals(nextTowerColour)){
            ArrayList<IslandGroup> toRemove = new ArrayList<>(); //support list
            toRemove.add(motherNaturePosition);
            toRemove.add(groups.get(previous_index));
            IslandGroup twoMerge = new IslandGroup(motherNaturePosition, groups.get(next_index));
            motherNaturePosition = twoMerge;
            groups.add(actualPosition.intValue(), twoMerge);
            groups.removeAll(toRemove);

        }else if(actualTowerColour.equals(previousTowerColour)){
            ArrayList<IslandGroup> toRemove = new ArrayList<>(); //support list
            toRemove.add(motherNaturePosition);
            toRemove.add(groups.get(previous_index));
            IslandGroup twoMerge = new IslandGroup(motherNaturePosition, groups.get(previous_index), groups.get(next_index));
            motherNaturePosition = twoMerge;
            groups.add(previous_index, twoMerge);
            groups.removeAll(toRemove);
        }
    }

}
