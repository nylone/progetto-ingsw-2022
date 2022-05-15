package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

import java.util.List;
import java.util.UUID;

public class LobbyAccept extends Response {

    private final List<Pair<UUID, String>> openLobbies;

    public LobbyAccept(StatusCode statusCode, List<Pair<UUID, String>> openLobbies) {
        super(statusCode);
        this.openLobbies = openLobbies;
    }

    public List<Pair<UUID, String>> getOpenLobbies() {
        return openLobbies;
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_LOBBY_ACCEPT;
    }
}
