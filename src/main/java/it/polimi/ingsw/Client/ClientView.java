package it.polimi.ingsw.Client;

import it.polimi.ingsw.Model.Enums.GamePhase;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.IslandField;
import it.polimi.ingsw.Model.IslandGroup;

import java.util.ArrayList;
import java.util.List;
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

    public void setGame(GameBoard game) {
        this.gameBoard = game;
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

    private void printIslandField() {
        ArrayList<IslandGroup> islandGroups = (ArrayList<IslandGroup>) gameBoard.getMutableIslandField().getMutableGroups();

    }

    private void drawIsland() {
        System.out.println("𝑰𝒔𝒍𝒂𝒏𝒅𝒔");
        List<IslandGroup> islandGroups = gameBoard.getMutableIslandField().getMutableGroups();
        for (IslandGroup ig : islandGroups) {

        }
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }
}


