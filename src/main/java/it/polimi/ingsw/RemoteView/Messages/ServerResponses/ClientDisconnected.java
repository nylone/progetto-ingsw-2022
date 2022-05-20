package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class ClientDisconnected extends Response {
    @Serial
    private static final long serialVersionUID = 304L;
    private final String nickname;

    public ClientDisconnected(String nickname) {
        super(StatusCode.Success);
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

}
