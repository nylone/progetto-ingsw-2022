package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.DuplicateElementException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Exceptions.Operation.ForbiddenOperationException;
import it.polimi.ingsw.Misc.SerializableOptional;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GamePhase;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Constants.OPERATION_NAME_PLAY_ASSISTANT;

/**
 * Represents the order in which players will play a round, and organizes the next round based on played {@link AssistantCard}s
 */
public class TurnOrder implements Serializable {
    @Serial
    private static final long serialVersionUID = 134L; // convention: 1 for model, (01 -> 99) for objects
    private final Map<PlayerBoard, SerializableOptional<AssistantCard>> selectedCards; // used to generate the new turn order
    // if a playerboard is associated with an empty optional then their card has not yet been chosen for the turn
    // or said player is currently being skipped
    private int currentTurnPosition; // selects the current player from currentTurnOrder
    private List<PlayerBoard> currentTurnOrder; // represents the order for the turn in play
    private GamePhase gamePhase;

    /**
     * Creates the turn order object and assigns a random starting turn formation for players.
     *
     * @param playerBoards the players in the game
     */
    public TurnOrder(List<PlayerBoard> playerBoards) {
        if (playerBoards != null && playerBoards.size() >= 2 && playerBoards.size() <= 4) {
            // add all players to their cards map and set them to not skipped
            this.selectedCards = new HashMap<>(playerBoards.size());
            for (PlayerBoard pb :
                    playerBoards) {
                this.selectedCards.put(pb, SerializableOptional.empty());
            }
            // create turn order
            this.currentTurnOrder = new ArrayList<>(playerBoards);
            Utils.shuffle(currentTurnOrder); // starting order for first turn is randomized
            // set current turn position
            this.currentTurnPosition = 0;
            // set game phase
            this.gamePhase = GamePhase.SETUP;
        } else {
            throw new RuntimeException("Inconsistent amount of Playerboards");
        }
    }

    /**
     * Get the current pecking order for the turn
     *
     * @return an Unmodifiable {@link List} ordered from index 0 being the first player, onwards
     */
    public List<PlayerBoard> getCurrentTurnOrder() {
        return List.copyOf(currentTurnOrder);
    }

    /**
     * Get the card a user played to define the pecking order
     *
     * @param pb the player to filter the played {@link AssistantCard}s for
     * @return a {@link SerializableOptional} containing the selected {@link AssistantCard}, if one has been played by the user this round.
     */
    public SerializableOptional<AssistantCard> getMutableSelectedCard(PlayerBoard pb) {
        return this.selectedCards.get(pb);
    }

    /**
     * Select the {@link AssistantCard} used by the player this round
     *
     * @param pb the player to set the card for
     * @param ac the card selected by the player
     * @throws ForbiddenOperationException if the card was already used, if the {@link GamePhase} is not in {@link GamePhase#SETUP}
     *                                     or if it's not the player's turn yet
     * @throws InvalidElementException     if the card or the player were null
     * @throws DuplicateElementException   if the player could have played a different, not yet played by him or anyone else (during this turn) card.
     */
    public void setSelectedCard(PlayerBoard pb, AssistantCard ac) throws ForbiddenOperationException, InvalidElementException, DuplicateElementException {
        if (pb == null) { // not null contract
            throw new InvalidElementException("PlayerBoard pb");
        }
        if (getGamePhase() != GamePhase.SETUP || !isOwnTurn(pb)) { // correct phase and turn contract
            throw new ForbiddenOperationException(OPERATION_NAME_PLAY_ASSISTANT);
        }
        if (ac == null) { // not null contract
            throw new InvalidElementException("AssistantCard ac");
        }
        if (ac.getUsed()) { // no reuse card contract
            throw new ForbiddenOperationException(OPERATION_NAME_PLAY_ASSISTANT);
        }
        if (isAlreadyInSelection(ac) && canPlayUniqueCard(pb)) { // no duplicate cards contract
            throw new DuplicateElementException("AssistantCard ac");
        }

        // validation passed:
        ac.setUsed();
        this.selectedCards.put(pb, SerializableOptional.of(ac));
    }

    /**
     * Get the phase of the current round
     *
     * @return the {@link GamePhase} of the current round
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     * Finds if it is a player's own turn yet
     *
     * @param pb the player to filter for
     * @return true if it is the player's turn, false otherwise
     */
    public boolean isOwnTurn(PlayerBoard pb) {
        return getMutableCurrentPlayer() == pb;
    }

    /**
     * Check if a card has already been played this round
     *
     * @param ac the card to filter for
     * @return true if the selected card was already submitted as a selection in {@link #setSelectedCard(PlayerBoard, AssistantCard)}
     * during this round
     */
    public boolean isAlreadyInSelection(AssistantCard ac) {
        return getSelectedCards().stream()
                .anyMatch(selected -> selected.getPriority() == ac.getPriority());
    }

    /**
     * Check to see if the player can still play a card that is unique this turn
     *
     * @param pb the player to filter cards for
     * @return true if the player can play at least one not yet selected card this round, false otherwise
     */
    public boolean canPlayUniqueCard(PlayerBoard pb) {
        return !pb.getMutableAssistantCards().stream().allMatch(this::isAlreadyInSelection);
    }

    /**
     * Get a reference to the current player
     *
     * @return a reference to the {@link PlayerBoard} of the current player
     */
    public PlayerBoard getMutableCurrentPlayer() {
        return this.currentTurnOrder.get(this.currentTurnPosition);
    }

    /**
     * Get all of the assistant cards played this round
     *
     * @return an Unmodifiable {@link List} of the {@link AssistantCard}s played this round as of yet
     */
    public List<AssistantCard> getSelectedCards() {
        return selectedCards.values().stream()
                .filter(SerializableOptional::isPresent)
                .map(SerializableOptional::get)
                .toList(); // immutable list
    }

    /**
     * Proceed to the next player in the turn order
     */
    public void stepToNextPlayer() {
        // for all players except the last in turn
        if (currentTurnPosition < currentTurnOrder.size() - 1) {
            currentTurnPosition++;
        } else { // last player to call this resets the turn and steps to next phase
            currentTurnPosition = 0;
            stepNextGamePhase();
        }
    }

    /**
     * During the round, switches between {@link GamePhase}s
     */
    private void stepNextGamePhase() {
        // if stepping from setup to action
        // there is a need to commit the new turn order
        if (getGamePhase() == GamePhase.SETUP) {
            commitTurnOrder();
            gamePhase = GamePhase.ACTION;
        } else { // when coming back to the setup phase the selected cards map must be reset
            cleanSelectedCards();
            gamePhase = GamePhase.SETUP;
        }
    }

    /**
     * Based on the {@link #getSelectedCards()} set the new turn order for the next round. Players that have not selected a card
     * will be put last in the order.
     */
    public void commitTurnOrder() {
        // the starting elements of playersInOrder are players that have not been skipped
        // the last elements of playersInOrder are all the players that have been skipped
        this.currentTurnOrder = selectedCards.entrySet().stream()
                .sorted(Comparator.comparingInt(t -> // sort based on priority
                        t.getValue()
                                .flatMap(ac -> SerializableOptional.of(
                                        ac.getPriority())) // if a card was selected extract the priority
                                .orElse(100))) // otherwise use a priority level that is higher than any other card
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Removes the selected cards from memory
     */
    private void cleanSelectedCards() {
        selectedCards.replaceAll((k, v) -> SerializableOptional.empty());
    }
}
