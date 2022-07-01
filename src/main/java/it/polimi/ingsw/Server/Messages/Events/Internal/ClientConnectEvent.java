package it.polimi.ingsw.Server.Messages.Events.Internal;

import java.util.List;

/**
 * This Event is generated when a lobby's client connects and is sent to the lobby's clients.
 * When handled by the {@link it.polimi.ingsw.Server.LobbyServer}, becomes {@link it.polimi.ingsw.Server.Messages.ServerResponses.ClientConnected}
 */
public class ClientConnectEvent extends ConnectEvent {

    /**
     * Create the event
     *
     * @param lastConnectedNickname the nickname of the player that just connected
     * @param players               the list of all connected players
     */
    public ClientConnectEvent(String lastConnectedNickname, List<String> players) {
        super(lastConnectedNickname, players);
    }

}
