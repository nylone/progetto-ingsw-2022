package it.polimi.ingsw.RemoteView.Messages.Events;

import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class CreateLobby extends ClientEvent implements MessageBuilder {
    private final boolean isPublic;
    private final int maxPlayers;

    public CreateLobby(boolean isPublic, int maxPlayers) {
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
