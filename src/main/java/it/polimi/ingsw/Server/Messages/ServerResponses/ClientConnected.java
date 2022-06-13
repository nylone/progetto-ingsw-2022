package it.polimi.ingsw.Server.Messages.ServerResponses;

import java.io.Serial;
import java.util.List;

public class ClientConnected extends FixedStatusResponse {
    @Serial
    private static final long serialVersionUID = 303L;
    private final String lastConnectedNickname;

    private final List<String> players;

    public ClientConnected(String lastConnectedNickname, List<String> players) {
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
