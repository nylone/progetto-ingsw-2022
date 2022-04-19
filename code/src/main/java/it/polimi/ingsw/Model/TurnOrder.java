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

    public TurnOrder(PlayerBoard... playerBoards) {
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

    public boolean isOwnTurn(PlayerBoard pb) {
        return getCurrentPlayer() == pb;
    }

    // this function verifies if the playerboard passed to the obj is valid
    private boolean isPlayerSubscribed(PlayerBoard pb) {
        return skippedPlayers.containsKey(pb);
    }

    public void addPlayerToSkip(PlayerBoard pb) {
        if (isPlayerSubscribed(pb)) {
            this.skippedPlayers.put(pb, true);
        }
    }

    public void removePlayerToSkip(PlayerBoard pb) {
        if (isPlayerSubscribed(pb)) {
            this.skippedPlayers.put(pb, false);
        }
    }

    public boolean isPlayerSkipped(PlayerBoard pb) {
        return skippedPlayers.get(pb);
    }

    public List<PlayerBoard> getSkippedPlayers() {
        return this.skippedPlayers.entrySet().stream()
                .filter(Map.Entry::getValue) // filter to only contain the skipped players
                .map(Map.Entry::getKey)
                .toList(); // returns unmodifiable List
    }

    public Optional<AssistantCard> getSelectedCard(PlayerBoard pb) {
        return this.selectedCards.get(pb);
    }

    private void cleanSelectedCards() {
        selectedCards.replaceAll((k, v) -> Optional.empty());
    }

    public void setSelectedCard(PlayerBoard pb, @NotNull AssistantCard ac) {
        if (getGamePhase() == GamePhase.SETUP &&
                isOwnTurn(pb)) {
            if (!ac.getUsed()) {
                ac.setUsed();
                this.selectedCards.put(pb, Optional.of(ac));
            } else {
                try {
                    throw new DuplicateAssistantCardException();
                } catch (DuplicateAssistantCardException e) {
                    e.printStackTrace();
                }
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
        currentTurnOrder = selectedCards.entrySet().stream()
                .filter(es -> es.getValue().isPresent())
                .sorted(Comparator.comparingInt(t -> t.getValue().get().getPriority()))
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<PlayerBoard> getCurrentTurnOrder() {
        return currentTurnOrder;
    }
}
