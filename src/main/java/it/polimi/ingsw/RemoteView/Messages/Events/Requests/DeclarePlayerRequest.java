package it.polimi.ingsw.RemoteView.Messages.Events.Requests;

import java.io.Serial;

public class DeclarePlayerRequest extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 354L;
    private final String nickname;
    private final String password;

    public DeclarePlayerRequest(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

}
