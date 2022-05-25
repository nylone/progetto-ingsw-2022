package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Misc.Symbols;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;

/**
 * This class contains the status of Client inside the Server and is responsible for printing UI elements in to the CLI
 */
public class ClientView {

    /**
     * Used to contain the game
     */
    GameBoard gameBoard;
    /**
     * Used to verify whether the client is connected to the Server
     */
    private boolean isConnected = false;
    /**
     * Used to verify whether the client is logged to the Server
     */
    private boolean isLogged = false;
    /**
     * Used to verify whether the client has entered a lobby
     */
    private boolean isInLobby = false;
    /**
     * Used to verify whether the Game has started
     */
    private boolean gameStarted = false;
    /**
     * Used to verify whether the Game has ended
     */
    private boolean gameEnded = false;
    /**
     * Used to contain lobby's admin's nickname
     */
    private String admin;
    /**
     * Used to contain client's nickname
     */
    private String Nickname;

    /**
     * Get lobby's admin's nickname
     * @return admin's nickname
     */
    public String getAdmin() {
        return admin;
    }

    /**
     * Get current game
     * @return gameBoard representing the game
     */
    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    /**
     * Get whether the game has started or not
     * @return true if the game has started, false otherwise
     */
    public boolean getGameStarted() {
        return this.gameStarted;
    }

    /**
     * get Client's nickname
     * @return String containing the nickname
     */
    public String getNickname() {
        return Nickname;
    }

    /**
     * Get whether the game has ended or not
     * @return true if the game has ended, false otherwise
     */
    public boolean isGameEnded() {
        return gameEnded;
    }
    /**
     * Get whether the client has connected to a lobby or not
     * @return true if the client has connected to a lobby, false otherwise
     */
    public boolean isInLobby() {
        return this.isInLobby;
    }

    /**
     * Get whether the client has connected to the Server or not
     * @return true if the client has connected to the Server, false otherwise
     */
    public boolean isConnected() {
        return isConnected;
    }
    /**
     * Get whether the client has logged to the Server or not
     * @return true if the client has logged to the Server, false otherwise
     */
    public boolean isLogged() {
        return isLogged;
    }

    /**
     * Set Game's admin's nickname
     * @param admin String containing admin's nickname
     */
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    /**
     * Set Game's status (started or not)
     * @param started boolean representing whether the game has started or not
     */
    public void setGameStarted(boolean started) {
        this.gameStarted = started;
    }

    /**
     * Set Game's status (ended or not)
     * @param gameEnded boolean representing whether the game has ended or not
     */
    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    /**
     * Set Client's connection status(connected or not)
     * @param connected boolean representing whether the client has connected or not to the server
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * Set game's model
     * @param game GameBoard containing game's model
     */
    public void setGame(GameBoard game) {
        this.gameBoard = game;
    }
    /**
     * Set Client's status inside a lobby (connected to a lobby or not)
     * @param inLobby boolean representing whether the client is in lobby or not
     */
    public void setIsInLobby(boolean inLobby) {
        this.isInLobby = inLobby;
    }
    /**
     * Set Client's logged status inside the server (logged or not)
     * @param logged boolean representing whether the client is logged or not
     */
    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    /**
     * Set Client's nickname
     * @param nickname String containing Client's nickname
     */
    public void setNickname(String nickname) {
        this.Nickname = nickname;
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

    public void disconnectView() {
        setIsInLobby(false);
        setAdmin(null);
        setGame(null);
        setGameStarted(false);
    }



}


