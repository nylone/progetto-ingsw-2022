package it.polimi.ingsw.Model;
import it.polimi.ingsw.Exceptions.DuplicateAssistantCardException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GamePhase;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class TurnOrder implements Serializable {
    private final Map<PlayerBoard, Optional<AssistantCard>> selectedCards; // used to generate the new turn order
    // if a playerboard is associated with an empty optional then their card has not yet been chosen for the turn
    // or said player is currently being skipped
    private final Map<PlayerBoard, Boolean> skippedPlayers; // players that need to be skipped in any turn are set to true
    private int currentTurnPosition; // selects the current player from currentTurnOrder
    private List<PlayerBoard> currentTurnOrder; // represents the order for the turn in play
    private GamePhase gamePhase;
    @Serial
    private static final long serialVersionUID = 134L; // convention: 1 for model, (01 -> 99) for objects

    public TurnOrder(@NotNull PlayerBoard... playerBoards) {
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

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public boolean isOwnTurn(@NotNull PlayerBoard pb) {
        return getCurrentPlayer() == pb;
    }

    // this function verifies if the playerboard passed to the obj is valid
    private boolean isPlayerSubscribed(@NotNull PlayerBoard pb) {
        return skippedPlayers.containsKey(pb);
    }

    public void addPlayerToSkip(@NotNull PlayerBoard pb) {
        if (isPlayerSubscribed(pb)) {
            this.skippedPlayers.put(pb, true);
        }
    }

    public void removePlayerToSkip(@NotNull PlayerBoard pb) {
        if (isPlayerSubscribed(pb)) {
            this.skippedPlayers.put(pb, false);
        }
    }

    public boolean isPlayerSkipped(@NotNull PlayerBoard pb) {
        return skippedPlayers.get(pb);
    }

    public List<PlayerBoard> getSkippedPlayers() {
        return this.skippedPlayers.entrySet().stream()
                .filter(Map.Entry::getValue) // filter to only contain the skipped players
                .map(Map.Entry::getKey)
                .toList(); // returns unmodifiable List
    }

    public Optional<AssistantCard> getSelectedCard(@NotNull PlayerBoard pb) {
        return this.selectedCards.get(pb);
    }

    public List<AssistantCard> getSelectedCards() {
        return selectedCards.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private void cleanSelectedCards() {
        selectedCards.replaceAll((k, v) -> Optional.empty());
    }

    private boolean isAlreadyInSelection(@NotNull AssistantCard ac) {
        return getSelectedCards().stream()
                .anyMatch(selected -> selected.getPriority() == ac.getPriority());
    }

    private boolean canOnlyPlayDuplicates(@NotNull AssistantCard[] acDeck) {
        return Arrays.stream(acDeck).allMatch(this::isAlreadyInSelection);
    }

    public void setSelectedCard(@NotNull PlayerBoard pb, @NotNull AssistantCard ac, @NotNull AssistantCard[] acDeck) {
        if (getGamePhase() == GamePhase.SETUP &&
                isOwnTurn(pb)) {
            if (!isAlreadyInSelection(ac) || canOnlyPlayDuplicates(acDeck)) {
                if (!ac.getUsed()) {
                    ac.setUsed();
                    this.selectedCards.put(pb, Optional.of(ac));
                } else {
                    // todo exception for already used card
                }
            } else {
                // todo exception for duplicate card
            }
        } else {
            // todo throw exception for out of turn access
        }
    }

    public PlayerBoard getCurrentPlayer() {
        return this.currentTurnOrder.get(this.currentTurnPosition);
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
        if (isPlayerSkipped(getCurrentPlayer()))
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

    public List<PlayerBoard> getCurrentTurnOrder() {
        return currentTurnOrder;
    }
}
