package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;

import java.util.UUID;

public class LobbyRedirect extends Response {
    private final UUID lobbyID;

    private LobbyRedirect(StatusCode statusCode, UUID lobbyID) {
        super(statusCode);
        this.lobbyID = lobbyID;
    }

    public static LobbyRedirect fail() {
        return new LobbyRedirect(StatusCode.Fail, null);
    }

    public static LobbyRedirect success(UUID lobbyID) {
        return new LobbyRedirect(StatusCode.Success, lobbyID);
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_LOBBY_REDIRECT;
    }
}
