package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.LobbyInfo;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;
import java.util.List;

public class LobbyAccept extends Response {
    @Serial
    private static final long serialVersionUID = 308L;

    private final List<LobbyInfo> publicLobbies;
    private final List<LobbyInfo> reconnectToTheseLobbies;

    public LobbyAccept(StatusCode statusCode, List<LobbyInfo> openLobbies, List<LobbyInfo> reconnectToTheseLobbies) {
        super(statusCode);
        this.publicLobbies = openLobbies;
        this.reconnectToTheseLobbies = reconnectToTheseLobbies;
    }

    public List<LobbyInfo> getReconnectToTheseLobbies() {
        return reconnectToTheseLobbies;
    }

    public List<LobbyInfo> getPublicLobbies() {
        return publicLobbies;
    }

}
