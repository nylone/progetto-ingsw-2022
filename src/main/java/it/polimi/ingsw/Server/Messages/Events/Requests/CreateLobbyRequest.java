package it.polimi.ingsw.Server.Messages.Events.Requests;

import java.io.Serial;

public class CreateLobbyRequest extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 353L;
    private final boolean isPublic;
    private final int maxPlayers;

    public CreateLobbyRequest(boolean isPublic, int maxPlayers) {
        this.isPublic = isPublic;
        this.maxPlayers = maxPlayers;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

}
