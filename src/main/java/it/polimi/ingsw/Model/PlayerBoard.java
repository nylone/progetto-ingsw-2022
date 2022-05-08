package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.Constants.*;

public class PlayerBoard implements Serializable {
    @Serial
    private static final long serialVersionUID = 126L; // convention: 1 for model, (01 -> 99) for objects
    private final String nickname;
    private final AssistantCard[] assistantCards;
    private final Map<PawnColour, Integer> diningRoom;
    private final List<Optional<PawnColour>> entrance;
    private final int id;
    private int coinBalance;

    public PlayerBoard(int id, int numOfPlayers, String nickname, StudentBag studentBag) {
        this.nickname = nickname;
        this.assistantCards = new AssistantCard[10];
        for (int i = 1; i <= 10; i++) {
            assistantCards[i - 1] = new AssistantCard(i);
        }
        this.coinBalance = 1;
        this.id = id;
        this.diningRoom = new EnumMap<>(PawnColour.class);
        for (PawnColour p : PawnColour.values()) {
            diningRoom.put(p, 0);
        }
        this.entrance = new ArrayList<>(numOfPlayers == 3 ? 9 : 7);
        if (numOfPlayers >= 2 && numOfPlayers <= 4) {
            for (int i = 0; i < (numOfPlayers == 3 ? 9 : 7) ; i++) {
                entrance.add(Optional.of(studentBag.extract()));
            }
        } else {
            throw new RuntimeException("Inconsistent number of players");
        }
    }

    // GETTERS //
    public List<AssistantCard> getMutableAssistantCards() {
        return Arrays.stream(assistantCards).toList();
    }

    public int getCoinBalance() {
        return coinBalance;
    }

    public Map<PawnColour, Integer> getDiningRoom() {
        return Map.copyOf(diningRoom);
    }

    public int getEntranceSpaceLeft() {
        return (int) entrance.stream()
                .filter(Optional::isEmpty)
                .count();
    }

    public int getEntranceSize() {
        return entrance.size();
    }

    public List<Optional<PawnColour>> getEntranceStudents() {
        return List.copyOf(entrance);
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    // SETTERS AND THE LIKE //
    protected boolean addStudentToDiningRoom(PawnColour colour) throws FullContainerException {
        if (this.diningRoom.get(colour) == 10) {
            throw new FullContainerException(CONTAINER_NAME_DININGROOM);
        }
        this.diningRoom.merge(colour, 1, Integer::sum);
        return this.diningRoom.get(colour) % 3 == 0; // returns true when a coin should be added
    }

    public void removeStudentsFromDiningRoom(PawnColour colour, int amount) throws EmptyContainerException {
        if (amount > 0) {
            if (this.getDiningRoomCount(colour) == 0) {
                throw new EmptyContainerException(CONTAINER_NAME_DININGROOM);
            } else {
                this.diningRoom.merge(colour, -amount, Integer::sum);
            }
        }
    }

    public int getDiningRoomCount(PawnColour colour) {
        return diningRoom.get(colour);
    }

    public boolean isDiningRoomFull(List<PawnColour> students) {
        Map<PawnColour, Integer> inputCount = new HashMap<>(5);
        students.forEach(s -> inputCount.merge(s, 1, Integer::sum));
        for (Map.Entry<PawnColour, Integer> entry : inputCount.entrySet()) {
            if (entry.getValue() + getDiningRoomCount(entry.getKey()) > 10) return true;
        }
        return false;
    }

    public void addStudentsToEntrance(List<PawnColour> students) throws FullContainerException {
        if (students.size() > this.getEntranceSpaceLeft()) {
            // 2 & 4 players -> 7 students placed on entrance, 3 players -> 9 students placed on entrance
            throw new FullContainerException(CONTAINER_NAME_ENTRANCE);
        }
        int cont=0;
        for(int i=0; i<this.getEntranceSize(); i++){
            if(this.entrance.get(i).isEmpty()){
                this.entrance.set(i,Optional.of(students.get(cont)));
                if(cont==students.size()-1){
                    break;
                }else{cont++;}
            }
        }
    }

    public void addStudentToEntrance(PawnColour student) throws FullContainerException {
        if (this.getEntranceSpaceLeft() + 1 > this.getEntranceSize()) {
            // 2 & 4 players -> 7 students placed on entrance, 3 players -> 9 students placed on entrance
            throw new FullContainerException(CONTAINER_NAME_ENTRANCE);
        }
        for (int i = 0; i < this.getEntranceSize(); i++) {
            if (this.entrance.get(i).isEmpty()) {
                this.entrance.set(i, Optional.of(student));
                return;
            }
        }
    }

    public void removeStudentFromEntrance(int pos) throws InvalidContainerIndexException {
        if (pos < 0 || pos >= this.getEntranceSize() || this.entrance.get(pos).isEmpty()) {
            throw new InvalidContainerIndexException(CONTAINER_NAME_ENTRANCE);
        }
        this.entrance.set(pos, Optional.empty());
    }

    public void removeStudentFromEntrance(PawnColour colour) throws InvalidElementException {
        for (int i = 0; i < this.getEntranceSize(); i++) {
            if (entrance.get(i).equals(Optional.of(colour))) {
                entrance.set(i, Optional.empty());
                return;
            }
        }
        throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_COLOUR);
    }

    protected void addCoin() {
        this.coinBalance += 1;
    }

    public void payCharacterEffect(int cost) { //this method checks if the CharacterCard can be activated, true --> gameBoard activates the CharacterCard / false--> GameBoard doesn't activate the CharacterCard
        if (this.coinBalance >= cost) {
            this.coinBalance -= cost;
        }
    }


    //test-purspose only

    /**
     * this method should only be called by a test
     * @param balance
     */
    public void setCoinBalance(int balance){
        this.coinBalance = balance;
    }
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

