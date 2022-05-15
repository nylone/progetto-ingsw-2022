package it.polimi.ingsw.RemoteView.Messages.Events.Requests;

import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;
import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

import java.util.UUID;

public class ConnectLobbyRequest extends ClientEvent implements MessageBuilder {
    private final UUID code;

    public ConnectLobbyRequest(UUID code) {
        this.code = code;
    }

    public UUID getCode() {
        return code;
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.REQUEST_CONNECT_LOBBY;
    }
}
