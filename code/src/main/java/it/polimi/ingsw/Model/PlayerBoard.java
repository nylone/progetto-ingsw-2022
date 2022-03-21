package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.FullDiningRoomException;
import it.polimi.ingsw.Exceptions.FullEntranceException;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class PlayerBoard {
    private int id;
    private final GameBoard context;
    private final AssistantCard[] assistantCards;
    private int coinBalance = 1;
    private final Map<PawnColour, Integer> diningRoom;
    private final ArrayList<PawnColour> entrance;
    private AssistantCard selectedAssistant;
    private final TowerColour towerColour;

    public PlayerBoard(GameBoard context, TowerColour towerColour) {
        this.context = context;
        this.towerColour = towerColour;
        assistantCards = new AssistantCard[10];
        diningRoom = new EnumMap<>(PawnColour.class);
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

    public int getDiningRoomCount(PawnColour colour) {
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

    public int getUnusedTower() {
        //todo
        return 0;
    }

    public void addStudentToDiningRoom(PawnColour colour) throws FullDiningRoomException {
        if (diningRoom.get(colour) != null && diningRoom.get(colour) == 10) {
            throw new FullDiningRoomException();
        } else {
            diningRoom.merge(colour, 1, Integer::sum);
            if (diningRoom.get(colour) % 3 == 0) {
                coinBalance++;
            }
        }
    }

    public void addStudentsToEntrance(ArrayList<PawnColour> students) throws FullEntranceException {
        if (entrance.size() + students.size() > 7) {
            throw new FullEntranceException();
        } else {
            entrance.addAll(students);
        }
    }

    public void PayCharacterEffect(int id) {
        coinBalance--;
    }
}
