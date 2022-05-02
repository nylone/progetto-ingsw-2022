package it.polimi.ingsw.Server.Messages.ClientMessages;

public class CreateLobby extends Request {
    private final boolean isPublic;
    private final byte maxPlayers;

    public CreateLobby(boolean isPublic, byte maxPlayers) {
        this.isPublic = isPublic;
        this.maxPlayers = maxPlayers;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public byte getMaxPlayers() {
        return maxPlayers;
    }
}
