package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;
import java.util.List;

public class ClientConnected extends Response {
    @Serial
    private static final long serialVersionUID = 303L;
    private final String lastConnectedNickname;

    private final List<String> players;

    public ClientConnected(String lastConnectedNickname, List<String> players) {
        super(StatusCode.Success);
        this.lastConnectedNickname = lastConnectedNickname;
        this.players = players;
    }

    public String getLastConnectedNickname() {
        return lastConnectedNickname;
    }

    public List<String> getPlayers() {
        return this.players;
    }


}
