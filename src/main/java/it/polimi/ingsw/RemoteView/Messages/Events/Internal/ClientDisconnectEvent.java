package it.polimi.ingsw.RemoteView.Messages.Events.Internal;


import java.util.List;

public class ClientDisconnectEvent extends ConnectEvent {

    public ClientDisconnectEvent(String lastDisconnectedNickname, List<String> players) {
        super(lastDisconnectedNickname, players);
    }
}
