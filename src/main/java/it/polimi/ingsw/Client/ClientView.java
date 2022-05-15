package it.polimi.ingsw.Client;

import it.polimi.ingsw.Model.GameBoard;

import java.util.UUID;

public class ClientView {

    GameBoard gameBoard;
    private boolean isLogged = false;
    private boolean isInLobby = false;
    private boolean gameStarted = false;
    private String admin;
    private String Nickname;
    private UUID lobbyID;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    public boolean getGameStarted() {
        return this.gameStarted;
    }

    public void setGameStarted(boolean started) {
        this.gameStarted = started;
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

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public boolean isInLobby() {
        return this.isInLobby;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

}
