package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import it.polimi.ingsw.Exceptions.FullDiningRoomException;

public class PlayerBoard {
    private int id;
    private GameBoard context;
    private AssistantCard[] assistantCards;
    private int coinBalance= 1;
    private Map<PawnColour, Integer> diningRoom;
    private ArrayList<PawnColour> entrance;
    private AssistantCard selectedAssistant;
    private TowerColour towerColour;

    public PlayerBoard(GameBoard context, TowerColour towerColour){
        this.context = context;
        this.towerColour = towerColour;
        assistantCards = new AssistantCard[10];
        diningRoom = new HashMap<>();
        entrance = new ArrayList<>();
    }

    public AssistantCard[] getAssistantCards() {
        return assistantCards;
    }

    public int getCoinBalance() {
        return coinBalance;
    }

    public Map<PawnColour, Integer> getDiningRoom() {
        return diningRoom;
    }

    public int getDiningRoomCount(PawnColour colour){
        return diningRoom.get(colour);
    }

    public ArrayList<PawnColour> getEntranceStudents() {
        return entrance;
    }

    public int getId() {
        return id;
    }

    public AssistantCard getSelectedAssistant() {
        return selectedAssistant;
    }

    public TowerColour getTowerColour() {
        return towerColour;
    }
    public int getUnusedTower(){
        //todo
        return 0;
    }
    public void addStudentToDiningRoom(PawnColour colour) throws FullDiningRoomException{
        if(diningRoom.get(colour) == 10){
            throw new FullDiningRoomException();
        }else {
            diningRoom.merge(colour, 1, Integer::sum);
            if (diningRoom.get(colour) % 3 == 0) {
                coinBalance++;
            }
        }
    }
    public void addStudentsToEntrance(ArrayList<PawnColour> students){
        entrance.addAll(students);
    }
    public void PayCharacterEffect(int id){
        coinBalance--;
    }
}
