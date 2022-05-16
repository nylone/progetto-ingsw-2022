package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Model.CharacterCard;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

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

    public boolean isInLobby() {
        return this.isInLobby;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    /**
     * Executes all the printing-methods (printIslandField... //todo complete)
     */
    public void printView() throws InvalidContainerIndexException, EmptyContainerException {
        printIslandField();
        System.out.println("\n");
        printGameBoards();
        printCharacterCard();
    }
    private void printCharacterCard() {
        if (this.gameBoard.getGameMode() == GameMode.SIMPLE) return;
        System.out.println("Available CharacterCards:");
        for(CharacterCard characterCard : this.gameBoard.getCharacterCards()){
            System.out.println("CharacterCard number:"+characterCard.getId() +" cost:"+characterCard.getCost());
        }
        System.out.println("\n");
    }


    /**
     * This method prints the islandField (islands and clouds)
     */
    private void printIslandField() throws InvalidContainerIndexException, EmptyContainerException {
        System.out.println(GameUI.draw(this.gameBoard));
    }

    private void printGameBoards() {
        System.out.println("PLAYERBOARDS");
        for (PlayerBoard pb : this.gameBoard.getMutablePlayerBoards()) {
            if (this.getNickname().equals(pb.getNickname())) {
                System.out.println("Your's Playerboard:");
            } else {
                System.out.println(pb.getNickname() + "'s Playerboard");
            }
            System.out.println(PlayerBoardUI.drawPlayerBoard(pb, this.gameBoard));
            System.out.println("\n");
        }
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }
}


