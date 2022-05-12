package it.polimi.ingsw.RemoteView.Messages.Events;

import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class ClientConnect extends ClientEvent implements MessageBuilder {
    private final String nickname;

    private final int numberOfPlayersConnected;

    public ClientConnect(String nickname, int numberOfPlayersConnected) {
        this.nickname = nickname;
        this.numberOfPlayersConnected = numberOfPlayersConnected;
    }

    public String getNickname() {
        return nickname;
    }

    public int getNumberOfPlayersConnected() {
        return numberOfPlayersConnected;
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_CLIENT_CONNECTED;
    }
}
