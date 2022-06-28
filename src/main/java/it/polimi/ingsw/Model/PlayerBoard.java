package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Exceptions.Operation.ForbiddenOperationException;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Misc.SerializableOptional;
import it.polimi.ingsw.Model.Enums.PawnColour;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.Constants.*;

/**
 * This class represents the Player's part of the board
 */
public class PlayerBoard implements Serializable {
    @Serial
    private static final long serialVersionUID = 126L; // convention: 1 for model, (01 -> 99) for objects
    private final String nickname;
    private final AssistantCard[] assistantCards;
    private final Map<PawnColour, Integer> diningRoom;
    private final List<SerializableOptional<PawnColour>> entrance;
    private final int id;
    private int coinBalance;

    /**
     * Generates a board and initializes its structures
     * @param id the ID of the player, used to reference the board through various classes.
     * @param numOfPlayers when creating a board, the number of players becomes important during initialization of various
     *                     internal structures
     * @param nickname every player has a nickname and the board can store it. Nicknames are not guaranteed to be unique, so
     *                 identifying a board through nicknames is highly insecure.
     * @param studentBag used during the initialization of the board.
     */
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
            for (int i = 0; i < (numOfPlayers == 3 ? 9 : 7); i++) {
                try {
                    entrance.add(SerializableOptional.of(studentBag.extract()));
                } catch (EmptyContainerException e) {
                    // should never happen
                    Logger.severe("student bag was found empty while adding a student to an student entrance. Critical, unrecoverable, error");
                    throw new RuntimeException(e);
                }
            }
        } else {
            throw new RuntimeException("Inconsistent number of players");
        }
    }

    /**
     * Get the {@link AssistantCard}s linked to the player
     * @return an Unmodifiable {@link List} of {@link AssistantCard} linked to the player
     */
    public List<AssistantCard> getMutableAssistantCards() {
        return Arrays.stream(assistantCards).toList();
    }

    /**
     * Get the current coin balance of the player
     * @return an integer representing the amount of coins owned by the player
     */
    public int getCoinBalance() {
        return coinBalance;
    }

    /**
     * Sets the coin balance for the PlayerBoard. <br>
     * NOTE: This method should only be called by tests
     *
     * @param balance the amount of coins you want to set the {@link PlayerBoard}'s balance to
     */
    public void setCoinBalance(int balance) {
        this.coinBalance = balance;
    }

    /**
     * Get the mappings from {@link PawnColour} to number of students of that colour in the Dining room
     * @return an Unmodifiable {@link Map} from {@link PawnColour} to {@link Integer}
     */
    public Map<PawnColour, Integer> getDiningRoom() {
        return Map.copyOf(diningRoom);
    }

    /**
     * Get a list of the active slots usable in the student entrance
     * @return an Unmodifiable {@link List} representing each slot of the Entrance
     */
    public List<SerializableOptional<PawnColour>> getEntranceStudents() {
        return List.copyOf(entrance);
    }

    /**
     * Get the ID of the player
     * @return the ID of the PlayerBoard
     */
    public int getId() {
        return id;
    }

    /**
     * Get the Nickname of the player
     * @return the Nickname of the PlayerBoard
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Unsafely add a student to the Dining room. The addition is unsafe because it doesn't track gained coins or teachers.
     * This method is used by {@link Model#addStudentToDiningRoom(PawnColour, PlayerBoard)} which is the "safe" version.
     * @param colour the colour of the student to add to the dining room
     * @return true if a new coin is to be added to the player's balance
     * @throws FullContainerException if the dining room is full on the lane of colour before the addition
     */
    protected boolean unsafeAddStudentToDiningRoom(PawnColour colour) throws FullContainerException {
        if (this.isDiningRoomFull(colour)) {
            throw new FullContainerException(CONTAINER_NAME_DININGROOM);
        }
        this.diningRoom.merge(colour, 1, Integer::sum);
        return this.diningRoom.get(colour) % 3 == 0; // returns true when a coin should be added
    }

    /**
     * Unsafely remove a student from the Dining room. The removal is unsafe because it doesn't track teachers.
     * This method is used by {@link Model#removeStudentFromDiningRoom(PawnColour, PlayerBoard)} which is the "safe" version.
     * @param colour the colour of the student to remove the dining room
     * @throws EmptyContainerException if the dining room is empty on the lane of colour before the removal
     */
    public void unsafeRemoveStudentFromDiningRoom(PawnColour colour) throws EmptyContainerException {
        if (this.getDiningRoomCount(colour) == 0) {
            throw new EmptyContainerException(CONTAINER_NAME_DININGROOM);
        } else {
            this.diningRoom.merge(colour, -1, Integer::sum);
        }
    }

    /**
     * Get the amount of students in a lane of the dining room
     * @param colour selects the lane of the dining room to inspect
     * @return the count of students in the lane selected by colour
     */
    public int getDiningRoomCount(PawnColour colour) {
        return diningRoom.get(colour);
    }

    /**
     * Check to see if the dining room can accommodate more students on a lane
     * @param student selects the lane of the dining room to inspect
     * @return true if the dining room's lane is full, false otherwise
     */
    public boolean isDiningRoomFull(PawnColour student) {
        return getDiningRoomCount(student) >= 10;
    }

    /**
     * Add multiple students to slots in the entrance
     * @param students a {@link List} of {@link PawnColour} containing the students to add to the entrance
     * @throws FullContainerException if the entrance isn's capable of holding all the students, the students are not added and the
     * exception is thrown
     */
    public void addStudentsToEntrance(List<PawnColour> students) throws FullContainerException {
        if (students.size() > this.getEntranceSpaceLeft()) {
            // 2 & 4 players -> 7 students placed on entrance, 3 players -> 9 students placed on entrance
            throw new FullContainerException(CONTAINER_NAME_ENTRANCE);
        }
        int cont = 0;
        for (int i = 0; i < this.getEntranceSize(); i++) {
            if (this.entrance.get(i).isEmpty()) {
                this.entrance.set(i, SerializableOptional.of(students.get(cont)));
                if (cont == students.size() - 1) {
                    break;
                } else {
                    cont++;
                }
            }
        }
    }

    /**
     * Get the amount of free slots in the student entrance
     * @return the count of free slots in the entrance
     */
    public int getEntranceSpaceLeft() {
        return (int) entrance.stream()
                .filter(SerializableOptional::isEmpty)
                .count();
    }

    /**
     * Get the size of the entrance (can change depending on the number of players)
     * @return the size of the entrance
     */
    public int getEntranceSize() {
        return entrance.size();
    }

    /**
     * Add a single student to a slot in the entrance
     * @param student a {@link PawnColour} representing the student to add to the entrance
     * @throws FullContainerException if the entrance isn's capable of the student, the student is not added and the
     * exception is thrown
     */
    public void addStudentToEntrance(PawnColour student) throws FullContainerException {
        if (this.getEntranceSpaceLeft() == 0) {
            // 2 & 4 players -> 7 students placed on entrance, 3 players -> 9 students placed on entrance
            throw new FullContainerException(CONTAINER_NAME_ENTRANCE);
        }
        for (int i = 0; i < this.getEntranceSize(); i++) {
            if (this.entrance.get(i).isEmpty()) {
                this.entrance.set(i, SerializableOptional.of(student));
                return;
            }
        }
    }

    /**
     * Removes a single student from the entrance
     * @param pos the index of the slot from which to remove the student
     * @return the {@link PawnColour} of the removed student
     * @throws InvalidContainerIndexException if the index is out of bounds or the slot was empty before removal
     */
    public PawnColour removeStudentFromEntrance(int pos) throws InvalidContainerIndexException {
        if (pos < 0 || pos >= this.getEntranceSize() || this.entrance.get(pos).isEmpty()) {
            throw new InvalidContainerIndexException(CONTAINER_NAME_ENTRANCE);
        }
        PawnColour student = this.entrance.get(pos).get();
        this.entrance.set(pos, SerializableOptional.empty());
        return student;
    }

    /**
     * Removes a single student from the entrance
     * @param colour the {@link PawnColour} of the student to remove from the entrance
     * @throws InvalidElementException if no students of the same colour could be found in the entrance
     */
    public void removeStudentFromEntrance(PawnColour colour) throws InvalidElementException {
        for (int i = 0; i < this.getEntranceSize(); i++) {
            if (entrance.get(i).equals(SerializableOptional.of(colour))) {
                entrance.set(i, SerializableOptional.empty());
                return;
            }
        }
        throw new InvalidElementException(INPUT_NAME_TARGET_PAWN_COLOUR);
    }

    /**
     * Adds a coin to the balance
     */
    protected void addCoin() {
        this.coinBalance += 1;
    }

    /**
     * Removes as many coins from the balance as the cost of the paid card.
     * @param card the {@link CharacterCard} to pay for
     * @throws ForbiddenOperationException if the balance cannot accommodate for the cost of the card
     */
    public void payCharacterEffect(CharacterCard card) throws ForbiddenOperationException {
        if (this.coinBalance >= card.getCost()) {
            this.coinBalance -= card.getCost();
        } else {
            throw new ForbiddenOperationException(OPERATION_NAME_PAY_CHARACTER_EFFECT);
        }
    }

    /*//
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
    //*/
}

