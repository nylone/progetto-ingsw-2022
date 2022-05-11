package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;

import java.util.UUID;

public class LobbyServerRedirect extends Response {
    private final UUID lobbyID;

    private LobbyServerRedirect(StatusCode statusCode, UUID lobbyID) {
        super(statusCode);
        this.lobbyID = lobbyID;
    }

    public static LobbyServerRedirect fail() {
        return new LobbyServerRedirect(StatusCode.Fail, null);
    }

    public static LobbyServerRedirect success(UUID lobbyID) {
        return new LobbyServerRedirect(StatusCode.Success, lobbyID);
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_LOBBY_SERVER_REDIRECT;
    }
}
