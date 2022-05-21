package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.RemoteView.Messages.Message;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.*;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;

public class ClientReader implements Runnable {
    /**
     * The socketWrapper used to receive messages from the server
     */
    private final SocketWrapper socketWrapper;
    /**
     * The object used to store the client's game data
     */
    private final ClientView clientView;
    /**
     * The reference to the CLIWriter class (used only in CLI mode)
     */
    private final CliWriter cli;

    CyclicBarrier cyclicBarrier;


    //CLI-only constructor
    public ClientReader(SocketWrapper socketWrapper, ClientView clientView, CliWriter cli, CyclicBarrier cyclicBarrier) {
        this.socketWrapper = socketWrapper;
        this.clientView = clientView;
        this.cli = cli;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {

        Message response;
        while (true) {
            try {
                response = socketWrapper.awaitMessage();
            } catch (IOException ex) {
                System.err.println("Server connection lost.");
                break;
            }
            if (response == null) {
                System.err.println("Server connection lost.");
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

    private void AnalyzeResponse(Message serverResponse) throws Exception {
        switch (serverResponse) {
            case Welcome welcome -> {
                if(welcome.getStatusCode() == StatusCode.Success){
                    ClearCLI();
                    System.out.println("Successfully connected to the server");
                    this.clientView.setConnected(true);
                }else{
                    System.out.println("Something gone wrong, connection not established");
                }
                this.cyclicBarrier.await();
            }
            case LobbyAccept response -> {
                if (response.getStatusCode() == StatusCode.Success) {
                    this.clientView.setLogged(true);
                    this.cyclicBarrier.await();
                    System.out.println("User accepted\n");
                    if (response.getPublicLobbies().size() == 0) {
                        System.out.println("No open lobbies available");
                    } else {
                        System.out.println("Available open lobbies:");
                        response.getPublicLobbies().forEach(lobbyInfo -> System.out.println("ID: " + lobbyInfo.getID() + " admin: " + lobbyInfo.getAdmin()));
                    }
                    System.out.println("type 'showActions' for a list of available actions during all the game");
                } else {
                    System.out.println("Password wrong for this username, try again or change Username");
                    this.cyclicBarrier.await();
                }
            }
            case LobbyRedirect response -> {
                UUID id = response.getLobbyID();
                if (response.getStatusCode() == StatusCode.Success) {
                    System.out.println("Joined to lobby, id: " + id + " admin:" + response.getAdmin());
                    clientView.setAdmin(response.getAdmin());
                } else {
                    System.out.println("Something gone wrong, lobby not joined");
                }
            }
            case LobbyClosed lobbyClosed -> {
                if(lobbyClosed.getStatusCode() == StatusCode.Success) {
                    ClearCLI();
                    System.out.println("The lobby has been closed; you can now join or create a lobby");
                    this.clientView.disconnectView();
                }else System.out.println("Something gone wrong, lobby not closed");
            }
            case ClientConnected clientConnected -> {
                if (clientConnected.getStatusCode() == StatusCode.Success) {
                    System.out.println("player " + clientConnected.getLastConnectedNickname() + " has connected");
                    System.out.println("Players connected:");
                    clientConnected.getPlayers().forEach(System.out::println);
                }
            }
            case ClientDisconnected clientDisconnected -> {
                if (clientDisconnected.getStatusCode() == StatusCode.Success) {
                    if(!this.clientView.getGameStarted()) {
                        System.out.println("player " + clientDisconnected.getLastDisconnectedNickname()+" has disconnected");
                        System.out.println("Players connected:");
                        clientDisconnected.getPlayers().forEach(System.out::println);
                    }
                }else{
                    System.out.println("Something gone wrong, client not disconnected");
                }
            }
            case GameInit response -> {
                if (response.getStatusCode() == StatusCode.Fail) {
                    System.out.println(response.getErrorMessage());
                } else {
                    System.out.println("Game is starting...");
                }
            }
            case GameStarted ignored -> {
                System.out.println("The game has started");
                clientView.setGameStarted(true);
            }

            case ModelUpdated modelUpdated -> {
                this.clientView.setGame(modelUpdated.getModel());
                UpdateView();

            }

            case PlayerActionFeedback response -> System.out.println(response.getReport());

            default -> System.out.println("Received an unexpected server's response:"+serverResponse.getClass());
        }
    }

    /**
     * This method clears Client's console to reprint it after an update
     */
    private void UpdateView() throws Exception {
        ClearCLI();
        this.clientView.printView();
    }

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

}
