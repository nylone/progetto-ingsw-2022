package it.polimi.ingsw.RemoteView.Messages.Events.Internal;

import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;

import java.util.List;

public class ConnectEvent implements ClientEvent {
    protected final String nickname;
    protected final List<String> players;

    public ConnectEvent(String affectedNickname, List<String> players) {
        this.nickname = affectedNickname;
        this.players = players;
    }

    public String getNickname() {
        return nickname;
    }

    public List<String> getPlayers() {
        return players;
    }

    public int getNumOfPlayersConnected() {
        return players.size();
    }
}
