package it.polimi.ingsw.Server.Messages.Events.Internal;

import it.polimi.ingsw.Server.Messages.Events.ClientEvent;

import java.util.List;

/**
 * This Event is generated when a lobby's client connects or disconnects and is sent to the lobby's clients.
 */
public class ConnectEvent implements ClientEvent {
    protected final String nickname;
    protected final List<String> players;

    /**
     * Create the event
     * @param affectedNickname the nickname of the player that just caused this event
     * @param players the list of all connected players
     */
    public ConnectEvent(String affectedNickname, List<String> players) {
        this.nickname = affectedNickname;
        this.players = players;
    }

    /**
     * Get the user that generated this event
     * @return the nickname of the user that just caused this event
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Get the players connected to the lobby
     * @return an Unmodifiable {@link List} containing players in the lobby
     */
    public List<String> getPlayers() {
        return players;
    }
}
