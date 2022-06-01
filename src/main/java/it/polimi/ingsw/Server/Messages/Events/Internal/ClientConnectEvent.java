package it.polimi.ingsw.Server.Messages.Events.Internal;

import java.util.List;

public class ClientConnectEvent extends ConnectEvent {

    public ClientConnectEvent(String lastConnectedNickname, List<String> players) {
        super(lastConnectedNickname, players);
    }

}
