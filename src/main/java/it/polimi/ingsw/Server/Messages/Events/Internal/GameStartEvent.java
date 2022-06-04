package it.polimi.ingsw.Server.Messages.Events.Internal;

import it.polimi.ingsw.Server.Messages.Events.ClientEvent;

import java.util.Map;

public class GameStartEvent implements ClientEvent {
    private final Map<String, Integer> nickToID;

    public GameStartEvent(Map<String, Integer> nickToID) {
        this.nickToID = Map.copyOf(nickToID);
    }

    public Map<String, Integer> getNickToID() {
        return nickToID;
    }
}
