package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class LobbyClosed extends Response {
    @Serial
    private static final long serialVersionUID = 309L;
    private final String nickname;

    public LobbyClosed(String nickname) {
        super(StatusCode.Success);
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

}
