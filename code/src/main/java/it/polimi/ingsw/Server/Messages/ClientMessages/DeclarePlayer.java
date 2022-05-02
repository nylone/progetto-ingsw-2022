package it.polimi.ingsw.Server.Messages.ClientMessages;

public class DeclarePlayer extends Request {
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
}
