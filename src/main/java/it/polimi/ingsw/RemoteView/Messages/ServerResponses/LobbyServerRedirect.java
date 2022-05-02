package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;

import java.util.UUID;

public class LobbyServerRedirect extends Response {
    private final UUID lobbyID;

    public LobbyServerRedirect(StatusCode statusCode, UUID lobbyID) {
        super(statusCode);
        this.lobbyID = lobbyID;
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_LOBBY_SERVER_REDIRECT;
    }
}
