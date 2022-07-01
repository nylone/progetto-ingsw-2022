package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Message;
import it.polimi.ingsw.Server.Messages.ServerResponses.*;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;

public class ClientReader implements Runnable {
    /**
     * The reference to the CLIWriter class (used only in CLI mode)
     */

    final CyclicBarrier cyclicBarrier;
    /**
     * The socketWrapper used to receive messages from the server
     */
    private final SocketWrapper socketWrapper;
    /**
     * The object used to store the client's game data
     */
    private final ClientView clientView;


    //CLI-only constructor
    public ClientReader(SocketWrapper socketWrapper, ClientView clientView, CyclicBarrier cyclicBarrier) {
        this.socketWrapper = socketWrapper;
        this.clientView = clientView;
        this.cyclicBarrier = cyclicBarrier;
    }

    /**
     * Keep listening the socket
     */
    @Override
    public void run() {
        //create Message object
        Message response;
        while (true) {
            try {
                //get message from Server
                response = socketWrapper.awaitMessage();
            } catch (IOException ex) {
                System.err.println("Server connection lost.");
                this.clientView.disconnectViewFromServer();
                break;
            }
            if (response == null) {
                System.err.println("Server connection lost.");
                this.clientView.disconnectViewFromServer();
                break;
            }

            //Elaborate the message from the server
            try {
                AnalyzeResponse(response);
            } catch (IOException e) {
                System.out.println("Error related to I/O ");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Method responsible for analyze Server's response and modify client's view basing on response
     * Furthermore it prints some useful information to update the player
     *
     * @param serverResponse message received from Server
     * @throws Exception Necessary to handle synchronization and view's update's exceptions
     */
    private void AnalyzeResponse(Message serverResponse) throws Exception {
        switch (serverResponse) {
            case PlayerActionFeedback playerActionFeedback -> {
                if (playerActionFeedback.getStatusCode() == StatusCode.Fail)
                    System.out.println(playerActionFeedback.getReport());
            }
            //Server's response received after requesting a connection
            case Welcome welcome -> {
                //check if Client was able to connect to Server
                if (welcome.getStatusCode() == StatusCode.Success) {
                    ClearCLI();
                    System.out.println("Successfully connected to the server");
                    //notify view that Client has connected
                    this.clientView.setConnected(true);
                } else {
                    System.out.println("Something gone wrong, connection not established");
                }
                //Notify CliWriter thread that Client has connected
                this.cyclicBarrier.await();
            }
            //Server's responde received after sending a DeclarePlayerRequest
            case LobbyServerAccept response -> {
                //check if client was able to log the Server
                if (response.getStatusCode() == StatusCode.Success) {
                    //notify view that Client has logged
                    this.clientView.setLogged(true);
                    System.out.println("User accepted\n");
                    //check for openLobbies availability
                    if (response.getPublicLobbies().size() == 0) {
                        System.out.println("No open lobbies available");
                    } else {
                        System.out.println("Available open lobbies:");
                        //print available openLobbies
                        response.getPublicLobbies().forEach(lobbyInfo -> System.out.println("ID: " + lobbyInfo.getID() + " admin: " + lobbyInfo.getAdmin()));
                    }
                    System.out.println("type 'showActions' for a list of available actions during all the game");
                } else {
                    System.out.println("Password wrong for this username, try again or change Username");
                }
                //Notify CliWriter thread that now Client has logged
                this.cyclicBarrier.await();
            }
            //Server's response received after sending a joinLobbyRequest or CreateLobbyRequest
            case LobbyConnected response -> {
                //check if client was able to join the selected lobby
                if (response.getStatusCode() == StatusCode.Success) {
                    //get Lobby's UUID
                    UUID id = response.getLobbyID();
                    System.out.println("Joined to lobby, id: " + id + " admin:" + response.getAdmin());
                    //update lobby's Admin's nickname inside Client's view
                    clientView.setAdmin(response.getAdmin());
                    //notify Client's view that Client has logged
                    clientView.setIsInLobby(true);
                } else {
                    System.out.println("Something gone wrong, lobby not joined");
                }
            }
            //Server's response received when the lobby has been closed for some reason
            case LobbyClosed lobbyClosed -> {
                //check if the lobby has been closed
                if (lobbyClosed.getStatusCode() == StatusCode.Success) {
                    if (!this.clientView.isGameEnded()) {
                        //if the lobby was closed before the end of the game clear cli before print any other message
                        ClearCLI();
                        System.out.println("The lobby has been closed; you can now join or create a lobby");
                        //notify lobby that Client left lobby and game
                        this.clientView.disconnectView();
                    } else {
                        System.out.println("\nThe lobby has been closed; you can now join or create a lobby");
                        //notify lobby that Client left lobby and game
                        this.clientView.disconnectView();
                    }
                } else System.out.println("Something gone wrong, lobby not closed");
            }
            //Server's response received when one player connected to the Lobby
            case ClientConnected clientConnected -> {
                if (clientConnected.getStatusCode() == StatusCode.Success) {
                    System.out.println("player " + clientConnected.getLastConnectedNickname() + " has connected");
                    System.out.println("Players connected:");
                    //print all connected players' nicknames
                    clientConnected.getPlayers().forEach(System.out::println);
                }
            }
            //Server's response received when one player disconnected from the Lobby
            case ClientDisconnected clientDisconnected -> {
                if (clientDisconnected.getStatusCode() == StatusCode.Success) {
                    //Only if the disconnection takes place before the game has started other waiting players should be notified of the disconnection
                    if (!this.clientView.getGameStarted()) {
                        System.out.println("player " + clientDisconnected.getLastDisconnectedNickname() + " has disconnected");
                        System.out.println("Players connected:");
                        //print all connected players' nicknames
                        clientDisconnected.getPlayers().forEach(System.out::println);
                    }
                } else {
                    System.out.println("Something gone wrong, client not disconnected");
                }
            }
            //Server's response received when Admin is starting the game
            case GameInit response -> {
                if (response.getStatusCode() == StatusCode.Fail) {
                    System.out.println(response.getErrorMessage());
                } else {
                    System.out.println("Game is starting...");
                }
            }
            //Server's response received when game has started
            case GameStarted ignored -> {
                System.out.println("The game has started");
                //notify Client's view that the game has started
                clientView.setGameStarted(true);
            }
            //Server's response containing updated model to show
            case ModelUpdated modelUpdated -> {
                //Update lobby's model
                this.clientView.setGame(modelUpdated.getModel());
                UpdateView();
            }
            //Server's response received when the game ended after a victory
            case GameOver gameOver -> {
                //notify Client's view that the game has ended
                this.clientView.setGameEnded(true);
                UpdateViewWin(gameOver.getWinners());
            }
            case InvalidRequest ignored ->
                    System.out.println("Something gone wrong, your request has not been executed");
            default -> System.out.println("Received an unexpected server's response:" + serverResponse.getClass());
        }
    }

    /**
     * This method clears Client's console
     */

    private void ClearCLI() {
        try {
            final String operatingSystem = System.getProperty("os.name");
            if (operatingSystem.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception e) {
            System.out.println("Clear operation failed");
        }
    }

    /**
     * Support method responsible for clearing CLI and print updated model by using view's printing methods, not used to print winners
     */
    private void UpdateView() {
        ClearCLI();
        this.clientView.printView();
    }

    /**
     * Support method responsible for printing game's winners
     *
     * @param winners list of String containing winners' nicknames
     */
    private void UpdateViewWin(List<String> winners) {
        ClearCLI();
        System.out.println("""

                 _       __        __                                          _                       ____
                | |     / /__     / /_  ____ __   _____     ____ _   _      __(_)___  ____  ___  _____/ / /
                | | /| / / _ \\   / __ \\/ __ `/ | / / _ \\   / __ `/  | | /| / / / __ \\/ __ \\/ _ \\/ ___/ / /\s
                | |/ |/ /  __/  / / / / /_/ /| |/ /  __/  / /_/ /   | |/ |/ / / / / / / / /  __/ /  /_/_/ \s
                |__/|__/\\___/  /_/ /_/\\__,_/ |___/\\___/   \\__,_/    |__/|__/_/_/ /_/_/ /_/\\___/_/  (_|_)  \s
                                                                                                          \s
                """);

        System.out.println("The winner is/are:");
        //print winners
        winners.forEach(System.out::println);
    }

}
