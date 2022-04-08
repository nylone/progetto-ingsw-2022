package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.EmptyDiningRoomException;
import it.polimi.ingsw.Exceptions.FullDiningRoomException;
import it.polimi.ingsw.Exceptions.FullEntranceException;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class PlayerBoard implements Serializable {
    @Serial
    private static final long serialVersionUID = 126L; // convention: 1 for model, (01 -> 99) for objects
    private final String nickname;
    private final AssistantCard[] assistantCards;
    private final Map<PawnColour, Integer> diningRoom;
    private final ArrayList<PawnColour> entrance;
    private final int maximum_entrance_students;
    private final int id;
    private int coinBalance;

    public PlayerBoard(int id, int numOfPlayers, String nickname, StudentBag studentBag) {
        this.nickname = nickname;
        this.assistantCards = new AssistantCard[10];
        for (int i = 0; i < 10; i++) {
            assistantCards[i] = new AssistantCard(i);
        }
        this.coinBalance = 1;
        this.id = id;
        this.diningRoom = new EnumMap<>(PawnColour.class);
        for (PawnColour p : PawnColour.values()) {
            diningRoom.put(p, 0);
        }
        this.entrance = new ArrayList<>();
        for (int i = 0; i < (numOfPlayers == 3 ? 9 : 7); i++) { // todo check playernum for consistency
            entrance.add(studentBag.extract());
        }
        this.maximum_entrance_students = this.entrance.size();
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

    public String getNickname() { return nickname; }


    public void addStudentToDiningRoom(PawnColour colour) throws FullDiningRoomException {
        if (this.diningRoom.get(colour) != null && this.diningRoom.get(colour) == 10) {
            throw new FullDiningRoomException();
        } else {
            this.diningRoom.merge(colour, 1, Integer::sum);
            if (this.diningRoom.get(colour) % 3 == 0) {
                this.coinBalance++;
            }
        }
    }
    public void removeStudentFromDiningRoom(PawnColour colour, int amount) throws EmptyDiningRoomException {
        if(this.getDiningRoomCount(colour)==0){
            throw new EmptyDiningRoomException("No students of that colour in DiningRoom");
        }else {
            this.diningRoom.merge(colour, -amount, Integer::sum);
        }
    }
    public void addStudentsToEntrance(ArrayList<PawnColour> students) throws FullEntranceException {
        if (this.entrance.size() + students.size() > maximum_entrance_students) { // 2 & 4 players -> 7 students placed on entrance, 3 players -> 9 students placed on entrance
            throw new FullEntranceException();
        } else {
            this.entrance.addAll(students);
        }
    }
    public boolean PayCharacterEffect(int id){ //this method checks if the CharacterCard can be activated, true --> gameBoard activates the CharacterCard / false--> GameBoard doesn't activate the CharacterCard
        if(this.coinBalance >= id) {
            coinBalance-= id;
            return true;
        }else {
            return false;
        }
    }

    //test-purspose only

    @Override
    public String toString() {
        return "PlayerBoard{" +
                "nickname='" + nickname + '\'' +
                ", assistantCards=" + Arrays.toString(assistantCards) +
                ", diningRoom=" + diningRoom +
                ", entrance=" + entrance +
                ", id=" + id +
                ", coinBalance=" + coinBalance +
                '}';
    }
}

