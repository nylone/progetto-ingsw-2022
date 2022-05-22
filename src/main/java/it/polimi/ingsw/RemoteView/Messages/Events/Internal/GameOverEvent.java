package it.polimi.ingsw.RemoteView.Messages.Events.Internal;

import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;

import java.util.List;

public class GameOverEvent implements ClientEvent {
    protected final List<String> winners;

    public GameOverEvent(List<String> winners) {
        this.winners = winners;
    }

    public List<String> getWinners() {
        return winners;
    }
}
