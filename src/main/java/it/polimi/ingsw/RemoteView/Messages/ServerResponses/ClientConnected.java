package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;
import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class ClientConnected extends Response {
    private final String nickname;

    public ClientConnected(String nickname) {
        super(StatusCode.Success);
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_CLIENT_CONNECTED;
    }
}
