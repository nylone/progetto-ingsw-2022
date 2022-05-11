package it.polimi.ingsw.RemoteView.Messages.Events;

import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class ClientConnect extends ClientEvent implements MessageBuilder {
    private final String nickname;

    public ClientConnect(String nickname) {
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
