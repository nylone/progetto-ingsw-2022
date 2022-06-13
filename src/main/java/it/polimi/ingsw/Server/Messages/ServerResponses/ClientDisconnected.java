package it.polimi.ingsw.Server.Messages.ServerResponses;

import java.io.Serial;
import java.util.List;

public class ClientDisconnected extends FixedStatusResponse {

    @Serial
    private static final long serialVersionUID = 304L;
    private final String lastDisconnectedNickname;

    private final List<String> players;

    public ClientDisconnected(String lastDisconnectedNickname, List<String> players) {
        this.lastDisconnectedNickname = lastDisconnectedNickname;
        this.players = players;
    }

    public int getNumOfPlayersConnected() {
        return players.size();
    }

    public List<String> getPlayers() {
        return this.players;
    }

    public String getLastDisconnectedNickname() {
        return this.lastDisconnectedNickname;
    }

}
