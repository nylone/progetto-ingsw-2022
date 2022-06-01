package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;
import java.util.UUID;

public class LobbyRedirect extends Response {
    @Serial
    private static final long serialVersionUID = 310L;
    private final UUID lobbyID;

    private final String admin;

    private LobbyRedirect(StatusCode statusCode, UUID lobbyID, String admin) {
        super(statusCode);
        this.lobbyID = lobbyID;
        this.admin = admin;
    }

    public static LobbyRedirect fail() {
        return new LobbyRedirect(StatusCode.Fail, null, null);
    }

    public static LobbyRedirect success(UUID lobbyID, String admin) {
        return new LobbyRedirect(StatusCode.Success, lobbyID, admin);
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public String getAdmin() {
        return admin;
    }

}
