package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Input.DuplicateElementException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Exceptions.Operation.ForbiddenOperationException;
import it.polimi.ingsw.Exceptions.Operation.OperationException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GamePhase;
import it.polimi.ingsw.Optional;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Constants.OPERATION_NAME_PLAY_ASSISTANT;

public class TurnOrder implements Serializable {
    @Serial
    private static final long serialVersionUID = 134L; // convention: 1 for model, (01 -> 99) for objects
    private final Map<PlayerBoard, Optional<AssistantCard>> selectedCards; // used to generate the new turn order
    // if a playerboard is associated with an empty optional then their card has not yet been chosen for the turn
    // or said player is currently being skipped
    private final Map<PlayerBoard, Boolean> skippedPlayers; // players that need to be skipped in any turn are set to true
    private int currentTurnPosition; // selects the current player from currentTurnOrder
    private List<PlayerBoard> currentTurnOrder; // represents the order for the turn in play
    private GamePhase gamePhase;

    public TurnOrder(PlayerBoard... playerBoards) {
        if (playerBoards.length >= 2 && playerBoards.length <= 4) {
            // add all players to their cards map and set them to not skipped
            this.selectedCards = new HashMap<>(playerBoards.length);
            this.skippedPlayers = new HashMap<>(playerBoards.length);
            for (PlayerBoard pb :
                    playerBoards) {
                this.selectedCards.put(pb, Optional.empty());
                this.skippedPlayers.put(pb, false);
            }
            // create turn order
            this.currentTurnOrder = Arrays.stream(playerBoards).collect(Collectors.toList());
            Utils.shuffle(currentTurnOrder); // starting order for first turn is randomized
            // set current turn position
            this.currentTurnPosition = 0;
            // set game phase
            this.gamePhase = GamePhase.SETUP;
        } else {
            throw new RuntimeException("Inconsistent amount of Playerboards");
        }
    }

    // GETTERS //
    public List<PlayerBoard> getCurrentTurnOrder() {
        return List.copyOf(currentTurnOrder);
    }

    public List<PlayerBoard> getSkippedPlayers() {
        return this.skippedPlayers.entrySet().stream()
                .filter(Map.Entry::getValue) // filter to only contain the skipped players
                .map(Map.Entry::getKey)
                .toList(); // returns unmodifiable List
    }

    public Optional<AssistantCard> getMutableSelectedCard(PlayerBoard pb) {
        return this.selectedCards.get(pb);
    }

    // cycles game-phases
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

    public void addPlayerToSkip(PlayerBoard pb) {
        if (isPlayerSubscribed(pb)) {
            this.skippedPlayers.put(pb, true);
        }
    }

    // this function verifies if the playerboard passed to the obj is valid
    private boolean isPlayerSubscribed(PlayerBoard pb) {
        return skippedPlayers.containsKey(pb);
    }

    public void removePlayerToSkip(PlayerBoard pb) {
        if (isPlayerSubscribed(pb)) {
            this.skippedPlayers.put(pb, false);
        }
    }

    public boolean isPlayerSkipped(PlayerBoard pb) {
        return skippedPlayers.get(pb);
    }

    private void cleanSelectedCards() {
        selectedCards.replaceAll((k, v) -> Optional.empty());
    }

    public void setSelectedCard(PlayerBoard pb, AssistantCard ac) throws OperationException, InputValidationException {
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
        if (isAlreadyInSelection(ac) && !canOnlyPlayDuplicates(pb)) { // no duplicate cards contract
            throw new DuplicateElementException("AssistantCard ac");
        }

        // validation passed:
        ac.setUsed();
        this.selectedCards.put(pb, Optional.of(ac));
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public boolean isOwnTurn(PlayerBoard pb) {
        return getMutableCurrentPlayer() == pb;
    }

    private boolean isAlreadyInSelection(AssistantCard ac) {
        return getSelectedCards().stream()
                .anyMatch(selected -> selected.getPriority() == ac.getPriority());
    }

    private boolean canOnlyPlayDuplicates(PlayerBoard pb) {
        return pb.getMutableAssistantCards().stream().allMatch(this::isAlreadyInSelection);
    }

    public PlayerBoard getMutableCurrentPlayer() {
        return this.currentTurnOrder.get(this.currentTurnPosition);
    }

    public List<AssistantCard> getSelectedCards() {
        return selectedCards.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList(); // immutable list
    }

    public void stepToNextPlayer() {
        // for all players except the last in turn
        if (currentTurnPosition < currentTurnOrder.size() - 1) {
            currentTurnPosition++;
        } else { // last player to call this resets the turn and steps to next phase
            currentTurnPosition = 0;
            stepNextGamePhase();
        }
        // if the new player in turn is to be skipped, recursively call the step function
        if (isPlayerSkipped(getMutableCurrentPlayer()))
            stepToNextPlayer();
    }

    public void commitTurnOrder() {
        // the starting elements of playersInOrder are players that have not been skipped
        // the last elements of playersInOrder are all the players that have been skipped
        this.currentTurnOrder = selectedCards.entrySet().stream()
                .sorted(Comparator.comparingInt(t -> // sort based on priority
                        t.getValue()
                                .flatMap(ac -> Optional.of(
                                        ac.getPriority())) // if a card was selected extract the priority
                                .orElse(100))) // otherwise use a priority level that is higher than any other card
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
