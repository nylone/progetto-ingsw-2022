package it.polimi.ingsw.RemoteView.Messages.ClientEvents;

import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

import java.util.UUID;

public class ConnectLobby extends ClientEvent implements MessageBuilder {
    private final UUID code;

    public ConnectLobby(UUID code) {
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
