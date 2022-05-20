package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.RemoteView.Messages.Message;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.*;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.IOException;
import java.util.UUID;

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

    //CLI-only constructor
    public ClientReader(SocketWrapper socketWrapper, ClientView clientView, CliWriter cli) {
        this.socketWrapper = socketWrapper;
        this.clientView = clientView;
        this.cli = cli;
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
            case LobbyAccept response -> {
                if (response.getStatusCode() == StatusCode.Success) {
                    this.clientView.setLogged(true);
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
                }
            }
            case LobbyRedirect response -> {
                UUID id = response.getLobbyID();
                if (response.getStatusCode() == StatusCode.Success) {
                    System.out.println("Joined to lobby, id: " + id + " admin:" + response.getAdmin());
                    clientView.setAdmin(response.getAdmin());
                    clientView.setLobbyID(id);
                } else {
                    System.out.println("Something gone wrong, lobby not joined");
                }
            }
            case ClientConnected response -> {
                if (response.getStatusCode() == StatusCode.Success) {
                    System.out.println("player " + response.getLastConnectedNickname() + " has connected");
                    System.out.println("players connected :" + response.getNumOfPlayersConnected());
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

            default -> throw new IllegalStateException("Unexpected value: " + serverResponse);
        }
    }

    /**
     * This method clears Client's console to reprint it after an update
     */
    private void UpdateView() throws Exception {
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
        this.clientView.printView();
    }
}
