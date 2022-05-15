package it.polimi.ingsw.RemoteView.Messages.Events.Internal;

import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;

public class ClientDisconnectEvent extends ClientEvent {
    private final String nickname;

    public ClientDisconnectEvent(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
