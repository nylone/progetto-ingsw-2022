package it.polimi.ingsw.RemoteView.Messages.ClientEvents;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class CreateLobby extends Event {
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

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.REQUEST_CREATE_LOBBY;
    }
}
