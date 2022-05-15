package it.polimi.ingsw.RemoteView.Messages.Events.Requests;

import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;
import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class CreateLobbyRequest extends ClientEvent implements MessageBuilder {
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

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.REQUEST_CREATE_LOBBY;
    }
}
