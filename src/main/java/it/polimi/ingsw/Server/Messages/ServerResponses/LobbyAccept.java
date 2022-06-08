package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.LobbyInfo;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;
import java.util.List;

public class LobbyAccept extends Response {
    @Serial
    private static final long serialVersionUID = 308L;

    private final List<LobbyInfo> publicLobbies;

    public LobbyAccept(StatusCode statusCode, List<LobbyInfo> openLobbies) {
        super(statusCode);
        this.publicLobbies = openLobbies;
    }

    public List<LobbyInfo> getPublicLobbies() {
        return publicLobbies;
    }

}
