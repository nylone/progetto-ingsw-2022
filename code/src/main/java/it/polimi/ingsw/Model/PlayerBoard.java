package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.FullDiningRoomException;
import it.polimi.ingsw.Exceptions.FullEntranceException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class PlayerBoard implements Serializable {
    private final String nickname;
    private final AssistantCard[] assistantCards;
    private final Map<PawnColour, Integer> diningRoom;
    private final ArrayList<PawnColour> entrance;
    private final TowerColour towerColour;
    private final int id;
    private int coinBalance;
    private AssistantCard selectedAssistant;

    public PlayerBoard(int id, int playerNum, String nickname, TowerColour towerColour, GameBoard context) {
        this.nickname = nickname;
        this.towerColour = towerColour;
        this.assistantCards = new AssistantCard[10];
        for (int i = 1; i <= 10; i++) {
            assistantCards[i] = new AssistantCard(i);
        }
        this.coinBalance = 1;
        this.id = id;
        this.diningRoom = new EnumMap<>(PawnColour.class);
        this.entrance = new ArrayList<>();
        for (int i = 0; i < playerNum; i++) { // todo check playernum for consistency
            entrance.add(context.getStudentBag().extract());
        }
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
