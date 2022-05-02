package it.polimi.ingsw.RemoteView.Messages.ClientEvents;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class DeclarePlayer extends Event {
    private final String nickname;
    private final String password;

    public DeclarePlayer(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.REQUEST_DECLARE_PLAYER;
    }
}
