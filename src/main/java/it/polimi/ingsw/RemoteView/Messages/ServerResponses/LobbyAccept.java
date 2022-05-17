package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

import java.util.List;
import java.util.UUID;

public class LobbyAccept extends Response {

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

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_LOBBY_ACCEPT;
    }
}
