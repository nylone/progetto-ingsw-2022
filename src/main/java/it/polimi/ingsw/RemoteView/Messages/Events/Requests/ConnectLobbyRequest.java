package it.polimi.ingsw.RemoteView.Messages.Events.Requests;

import java.io.Serial;
import java.util.UUID;

public class ConnectLobbyRequest extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 352L;

    private final UUID code;

    public ConnectLobbyRequest(UUID code) {
        this.code = code;
    }

    public UUID getCode() {
        return code;
    }

}
