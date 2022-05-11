package it.polimi.ingsw.Client;

import java.util.UUID;

public class ClientView {


    private boolean isInLobby = false;

    private UUID lobbyID;

    public boolean isInLobby() {
        return isInLobby;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
        setIsInLobby(true);
    }

    public void setIsInLobby(boolean inLobby) {
        this.isInLobby = inLobby;
    }

}
