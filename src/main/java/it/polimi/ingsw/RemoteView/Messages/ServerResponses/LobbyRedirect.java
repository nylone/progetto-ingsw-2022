package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.util.UUID;

public class LobbyRedirect extends Response {
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

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_LOBBY_REDIRECT;
    }
}
