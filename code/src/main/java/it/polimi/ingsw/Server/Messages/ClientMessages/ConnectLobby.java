package it.polimi.ingsw.Server.Messages.ClientMessages;

public class ConnectLobby extends Request {
    private final long code;

    public ConnectLobby(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }
}
