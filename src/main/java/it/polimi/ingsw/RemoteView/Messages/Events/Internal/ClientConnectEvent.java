package it.polimi.ingsw.RemoteView.Messages.Events.Internal;

import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;

public class ClientConnectEvent extends ClientEvent {
    private final String nickname;

    private final int numberOfPlayersConnected;

    public ClientConnectEvent(String nickname, int numberOfPlayersConnected) {
        this.nickname = nickname;
        this.numberOfPlayersConnected = numberOfPlayersConnected;
    }

    public String getNickname() {
        return nickname;
    }

    public int getNumberOfPlayersConnected() {
        return numberOfPlayersConnected;
    }
}
