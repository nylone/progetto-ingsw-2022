package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;
import java.util.List;

public class ClientDisconnected extends Response {

    @Serial
    private static final long serialVersionUID = 304L;
    private final String lastDisconnectedNickname;

    private final List<String> players;

    public ClientDisconnected(String lastDisconnectedNickname, List<String> players) {
        super(StatusCode.Success);
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
