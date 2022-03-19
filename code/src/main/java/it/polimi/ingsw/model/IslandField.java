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
        motherNaturePosition = groups.get(0);
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
        AtomicInteger actualPosition2 = new AtomicInteger(actualPosition.intValue());
        int previous_index = actualPosition.accumulateAndGet(1, (index,dec) -> (--index < 0 ? groups.size() : index));
        int next_index = actualPosition2.accumulateAndGet(1, (index,acc) -> (++index > groups.size() ? 0 : index));
        Optional<TowerColour> actualTowerColour = motherNaturePosition.getIslands().get(0).getTower(); //get the towerColour of actual IslandGroup
        Optional<TowerColour> previousTowerColour = groups.get(previous_index).getIslands().get(0).getTower(); //get the towerColour of previous IslandGroup
        Optional<TowerColour> nextTowerColour = groups.get(next_index).getIslands().get(0).getTower(); //get the towerColour of next islandGroup
        if(actualTowerColour.equals(nextTowerColour) && actualTowerColour.equals(previousTowerColour)){ //merge three different islands
            System.out.println(groups.indexOf(motherNaturePosition));
            System.out.println(previous_index);
            System.out.println(next_index);
            ArrayList<IslandGroup> toRemove = new ArrayList<>(); //support list
            toRemove.add(motherNaturePosition);
            toRemove.add(groups.get(previous_index));
            toRemove.add(groups.get(next_index));
            IslandGroup threeMerge = new IslandGroup(groups.get(previous_index),motherNaturePosition, groups.get(next_index));
            motherNaturePosition = threeMerge;
            groups.add(previous_index,threeMerge); //set the new GroupIsland at the lowest index of the three groupIslands
            groups.removeAll(toRemove); //remove old GroupIslands

        }else if(actualTowerColour.equals(nextTowerColour)){
            ArrayList<IslandGroup> toRemove = new ArrayList<>(); //support list
            toRemove.add(motherNaturePosition);
            toRemove.add(groups.get(next_index));
            //System.out.println(toRemove+"\n");
            IslandGroup twoMerge = new IslandGroup(motherNaturePosition, groups.get(next_index));
            groups.add(groups.indexOf(motherNaturePosition), twoMerge);
            groups.removeAll(toRemove);
            motherNaturePosition = twoMerge;


        }else if(actualTowerColour.equals(previousTowerColour)){
            System.out.println("PREVIOUS");
            ArrayList<IslandGroup> toRemove = new ArrayList<>(); //support list
            toRemove.add(motherNaturePosition);
            toRemove.add(groups.get(previous_index));
            IslandGroup twoMerge = new IslandGroup(groups.get(previous_index), motherNaturePosition);
            motherNaturePosition = twoMerge;
            groups.add(previous_index, twoMerge);
            groups.removeAll(toRemove);
        }
    }
    //test-purpose only

    @Override
    public String toString() {
        return "IslandField{" +
                ", groups=" + groups +
                ", islands=" + islands +
                ", motherNaturePosition=" + motherNaturePosition +
                '}';
    }
}