package it.polimi.ingsw.Model;
// todo create interface to make the turn order modifiable only by the playerboards and readable from the game handler

import it.polimi.ingsw.Exceptions.DuplicateAssistantCardException;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class TurnOrder implements Serializable {
    private final Map<PlayerBoard, AssistantCard> selectedCards;
    private PlayerBoard currentPlayer;
    @Serial
    private static final long serialVersionUID = 134L; // convention: 1 for model, (01 -> 99) for objects

    public TurnOrder() {
        this.selectedCards = new HashMap<>();
    }

    public Optional<AssistantCard> getSelectedCard(PlayerBoard player) {
        // todo add exception for user not in map
        return Optional.ofNullable(this.selectedCards.get(player));
    }

    public void setSelectedCard(PlayerBoard p, AssistantCard ac) {
        if (currentPlayer.equals(p)) {
            if (!ac.getUsed()) {
                ac.use();
                this.selectedCards.put(p, ac);
                this.setNext();
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

    public void setNext() {
        this.currentPlayer = getNext().orElse(null);
    }

    public List<Map.Entry<PlayerBoard, AssistantCard>> getTurnOrder() {
        return this.selectedCards.entrySet().stream().sorted(
                Comparator.comparingInt(t -> t.getValue().getPriority())
        ).collect(Collectors.toList());
    }
}
