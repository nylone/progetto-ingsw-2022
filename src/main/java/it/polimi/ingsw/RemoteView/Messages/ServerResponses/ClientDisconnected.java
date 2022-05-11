package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;
import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class ClientDisconnected extends Response implements MessageBuilder {
    private final String nickname;

    public ClientDisconnected(String nickname) {
        super(StatusCode.Success);
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_CLIENT_DISCONNECTED;
    }
}
