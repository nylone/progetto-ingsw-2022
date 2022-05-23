package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Misc.Symbols;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;


public class ClientView {

    GameBoard gameBoard;

    private boolean isConnected = false;
    private boolean isLogged = false;
    private boolean isInLobby = false;
    private boolean gameStarted = false;
    private boolean gameEnded = false;
    private String admin;
    private String Nickname;


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

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
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
                System.out.println("Your Playerboard:");
            } else {
                System.out.println(pb.getNickname() + "'s Playerboard");
            }
            System.out.println(PlayerBoardUI.drawPlayerBoard(pb, this.gameBoard));
            System.out.println("\n");
        }
        System.out.println(InfoUI.draw(this.gameBoard, this.Nickname));
    }

    private void printCharacterCard() {
        if (this.gameBoard.getGameMode() == GameMode.SIMPLE) return;
        System.out.println("Available CharacterCards:");
        for (CharacterCard characterCard : this.gameBoard.getCharacterCards()) {
            System.out.print("CharacterCard number:" + characterCard.getId() + " cost:" + characterCard.getCost());
            if (characterCard instanceof StatefulEffect) {
                System.out.print("\t");
                for (Object o : ((StatefulEffect) characterCard).getState()) {
                    switch (o) {
                        case NoEntryTile ignored -> System.out.print(" X ");
                        case PawnColour pawnColour ->
                                System.out.print(Symbols.colorizeStudent(pawnColour, "  " + Symbols.PAWN));
                        case default -> System.out.println("Card's object not valid");
                    }
                }
            }
            System.out.println("\n");
        }
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public void disconnectView() {
        setIsInLobby(false);
        setAdmin(null);
        setGame(null);
        setGameStarted(false);
    }

    public void setIsInLobby(boolean inLobby) {
        this.isInLobby = inLobby;
    }

    public void setGame(GameBoard game) {
        this.gameBoard = game;
    }

}


