package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class ClientConnected extends Response {
    @Serial
    private static final long serialVersionUID = 303L;
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

}
