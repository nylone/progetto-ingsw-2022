package it.polimi.ingsw.Client.CLI;

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
     *
     * @return admin's nickname
     */
    public String getAdmin() {
        return admin;
    }

    /**
     * Set Game's admin's nickname
     *
     * @param admin String containing admin's nickname
     */
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    /**
     * Get current game
     *
     * @return gameBoard representing the game
     */
    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    /**
     * Get whether the game has started or not
     *
     * @return true if the game has started, false otherwise
     */
    public boolean getGameStarted() {
        return this.gameStarted;
    }

    /**
     * Set Game's status (started or not)
     *
     * @param started boolean representing whether the game has started or not
     */
    public void setGameStarted(boolean started) {
        this.gameStarted = started;
    }

    /**
     * Get whether the game has ended or not
     *
     * @return true if the game has ended, false otherwise
     */
    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * Set Game's status (ended or not)
     *
     * @param gameEnded boolean representing whether the game has ended or not
     */
    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    /**
     * Get whether the client has connected to a lobby or not
     *
     * @return true if the client has connected to a lobby, false otherwise
     */
    public boolean isInLobby() {
        return this.isInLobby;
    }

    /**
     * Get whether the client has connected to the Server or not
     *
     * @return true if the client has connected to the Server, false otherwise
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Set Client's connection status(connected or not)
     *
     * @param connected boolean representing whether the client has connected or not to the server
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * Get whether the client has logged to the Server or not
     *
     * @return true if the client has logged to the Server, false otherwise
     */
    public boolean isLogged() {
        return isLogged;
    }

    /**
     * Set Client's logged status inside the server (logged or not)
     *
     * @param logged boolean representing whether the client is logged or not
     */
    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    /**
     * Prints all gameBoard's element that the user is allowed to see (Islands, clouds, playerBoards and characterCards)
     */
    public void printView() {
        printIslandField();
        System.out.println("\n");
        printGameBoards();
        printCharacterCard();
    }

    /**
     * Support method responsible for printing all field's components (islands and clouds)
     */
    private void printIslandField() {
        //print GameBoard
        System.out.println(GameUI.draw(this.gameBoard));
    }

    /**
     * Support method responsible for printing all players' PlayerBoards
     */
    private void printGameBoards() {
        System.out.println("PLAYERBOARDS");
        //simple loop to print playerBoard and its Owner's nickname
        for (PlayerBoard pb : this.gameBoard.getMutablePlayerBoards()) {
            if (this.getNickname().equals(pb.getNickname())) {
                System.out.println("Your Playerboard:");
            } else {
                System.out.println(pb.getNickname() + "'s Playerboard");
            }
            //print PlayerBoard
            System.out.println(PlayerBoardUI.drawPlayerBoard(pb, this.gameBoard));
            System.out.println("\n");
        }
        //print current and next player
        System.out.println(InfoUI.draw(this.gameBoard, this.Nickname));
    }

    /**
     * Support method responsible for printing the 3 characterCards
     */
    private void printCharacterCard() {
        //Print only if the gameMode is Advanced
        if (this.gameBoard.getGameMode() == GameMode.SIMPLE) return;
        System.out.println("Available CharacterCards:");
        for (CharacterCard characterCard : this.gameBoard.getCharacterCards()) {
            //print CharacterCard's number and cost
            System.out.print("CharacterCard number:" + characterCard.getId() + " cost:" + characterCard.getCost());
            //only if the CharacterCard has a stateful effect then print it's content
            if (characterCard instanceof StatefulEffect) {
                System.out.print("\t");
                for (Object o : ((StatefulEffect) characterCard).getState()) {
                    switch (o) {
                        //if the content is a NoENtryTile then print 'X'
                        case NoEntryTile ignored -> System.out.print(" X ");
                        //if the content is a pawn then print it's colour
                        case PawnColour pawnColour ->
                                System.out.print(Symbols.colorizeStudent(pawnColour, "  " + Symbols.PAWN));
                        case default -> System.out.println("Card's object not valid");
                    }
                }
            }
            System.out.println("\n");
        }
    }

    /**
     * get Client's nickname
     *
     * @return String containing the nickname
     */
    public String getNickname() {
        return Nickname;
    }

    /**
     * Set Client's nickname
     *
     * @param nickname String containing Client's nickname
     */
    public void setNickname(String nickname) {
        this.Nickname = nickname;
    }

    /**
     * Method to disconnect the view from lobby (when the game ends or is closed for any reason)
     */
    public void disconnectView() {
        setIsInLobby(false);
        setAdmin(null);
        setGame(null);
        setGameStarted(false);
    }

    /**
     * Set Client's status inside a lobby (connected to a lobby or not)
     *
     * @param inLobby boolean representing whether the client is in lobby or not
     */
    public void setIsInLobby(boolean inLobby) {
        this.isInLobby = inLobby;
    }

    /**
     * Set game's model
     *
     * @param game GameBoard containing game's model
     */
    public void setGame(GameBoard game) {
        this.gameBoard = game;
    }


}


