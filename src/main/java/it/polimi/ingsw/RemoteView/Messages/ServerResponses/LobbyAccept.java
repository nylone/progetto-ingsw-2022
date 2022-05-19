package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.LobbyInfo;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.util.List;

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
