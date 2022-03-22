package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class TurnOrder implements Serializable {
    private final Map<PlayerBoard, AssistantCard> selectedCards;
    private PlayerBoard currentPlayer;

    public TurnOrder() {
        this.selectedCards = new HashMap<>();
    }

    public Optional<AssistantCard> getSelectedCard(PlayerBoard player) {
        // todo add exception for user not in map
        return Optional.ofNullable(this.selectedCards.get(player));
    }

    public void setSelectedCard(PlayerBoard player, AssistantCard selection) {
        this.selectedCards.put(player, selection);
    }

    public PlayerBoard getCurrent() {
        return this.currentPlayer;
    }

    public Optional<PlayerBoard> getNext() {
        AssistantCard currentCard = this.selectedCards.get(getCurrent());
        PlayerBoard nextPlayer = null;
        AssistantCard nextCard = null;
        for (PlayerBoard extractedPlayer :
                this.selectedCards.keySet()) {
            AssistantCard extractedCard = this.selectedCards.get(extractedPlayer);
            if (extractedCard.getPriority() > currentCard.getPriority() && (nextPlayer == null || extractedCard.getPriority() < nextCard.getPriority())) {
                nextPlayer = extractedPlayer;
                nextCard = extractedCard;
            }
        }
        return Optional.ofNullable(nextPlayer);
    }

    public List<Map.Entry<PlayerBoard, AssistantCard>> getTurnOrder() {
        return this.selectedCards.entrySet().stream().sorted(
                Comparator.comparingInt(t -> t.getValue().getPriority())
        ).collect(Collectors.toList());
    }
}
