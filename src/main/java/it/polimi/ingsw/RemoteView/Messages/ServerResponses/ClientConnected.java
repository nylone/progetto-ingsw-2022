package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;
import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class ClientConnected extends Response {
    private final String nickname;

    private final int playersConnected;

    public ClientConnected(String nickname, int playersConnected) {
        super(StatusCode.Success);
        this.nickname = nickname;
        this.playersConnected = playersConnected;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPlayersConnected() {
        return playersConnected;
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_CLIENT_CONNECTED;
    }
}
