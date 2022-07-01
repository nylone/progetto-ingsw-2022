package it.polimi.ingsw.Server.Messages.Events.Internal;


import java.util.List;

/**
 * This Event is generated when a lobby's client disconnects and is sent to the lobby's clients.
 * When handled by the {@link it.polimi.ingsw.Server.LobbyServer}, becomes {@link it.polimi.ingsw.Server.Messages.ServerResponses.ClientConnected}
 */
public class ClientDisconnectEvent extends ConnectEvent {

    /**
     * Create the event
     *
     * @param lastDisconnectedNickname the nickname of the player that just disconnected
     * @param players                  the list of all connected players
     */
    public ClientDisconnectEvent(String lastDisconnectedNickname, List<String> players) {
        super(lastDisconnectedNickname, players);
    }
}
