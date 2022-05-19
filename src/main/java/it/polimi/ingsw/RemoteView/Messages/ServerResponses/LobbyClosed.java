package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

public class LobbyClosed extends Response {
    private final String nickname;

    public LobbyClosed(String nickname) {
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
