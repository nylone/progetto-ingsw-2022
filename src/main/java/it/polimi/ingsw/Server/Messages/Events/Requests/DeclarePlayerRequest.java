package it.polimi.ingsw.Server.Messages.Events.Requests;

import java.io.Serial;

public class DeclarePlayerRequest extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 354L;
    private final String nickname;

    public DeclarePlayerRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
