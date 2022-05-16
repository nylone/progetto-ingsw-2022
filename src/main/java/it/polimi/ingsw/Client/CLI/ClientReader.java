package it.polimi.ingsw.Client.CLI;

import com.google.gson.Gson;
import it.polimi.ingsw.RemoteView.Messages.Message;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.*;
import it.polimi.ingsw.RemoteView.SocketWrapper;

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
        switch (serverResponse.getType()) {
            case RESPONSE_LOBBY_ACCEPT -> {
                LobbyAccept response = new Gson().fromJson(serverResponse.getData(), LobbyAccept.class);
                if (response.getStatusCode() == StatusCode.Success) {
                    this.clientView.setLogged(true);
                    System.out.println("User accepted\n");
                    if (response.getOpenLobbies().size() == 0) {
                        System.out.println("No open lobbies available");
                    } else {
                        System.out.println("Available open lobbies:");
                        response.getOpenLobbies().forEach(uuidStringPair -> System.out.println("ID: " + uuidStringPair.getFirst() + " admin: " + uuidStringPair.getSecond()));
                    }
                    System.out.println("type 'showActions' for a list of available actions during all the game");
                } else {
                    System.out.println("Password wrong for this username, try again or change Username");
                }
            }
            case RESPONSE_LOBBY_REDIRECT -> {
                LobbyRedirect response = new Gson().fromJson(serverResponse.getData(), LobbyRedirect.class);
                UUID id = response.getLobbyID();
                if (response.getStatusCode() == StatusCode.Success) {
                    System.out.println("Joined to lobby, id: " + id + " admin:" + response.getAdmin());
                    clientView.setAdmin(response.getAdmin());
                    clientView.setLobbyID(id);
                } else {
                    System.out.println("Something gone wrong, lobby not joined");
                }
            }
            case RESPONSE_CLIENT_CONNECTED -> {
                ClientConnected response = new Gson().fromJson(serverResponse.getData(), ClientConnected.class);
                if (response.getStatusCode() == StatusCode.Success) {
                    System.out.println("player " + response.getNickname() + " has connected");
                    System.out.println("players connected :" + response.getPlayersConnected());
                }
            }
            case RESPONSE_GAME_INIT -> {
                GameInit response = new Gson().fromJson(serverResponse.getData(), GameInit.class);
                if (response.getStatusCode() == StatusCode.Fail) {
                    System.out.println(response.getErrorMessage());
                } else {
                    System.out.println("Game is starting...");
                }
            }
            case RESPONSE_GAME_STARTED -> {
                System.out.println("The game has started");
                clientView.setGameStarted(true);
            }

            case RESPONSE_MODEL_UPDATED -> {
                ModelUpdated modelUpdated = new Gson().fromJson(serverResponse.getData(), ModelUpdated.class);
                this.clientView.setGame(modelUpdated.getModel());
                UpdateView();

            }

            case RESPONSE_PLAYER_ACTION -> {
                PlayerActionFeedback response = new Gson().fromJson(serverResponse.getData(), PlayerActionFeedback.class);
                System.out.println(response.getReport());
            }

        }
    }

    /**
     * This method clears Client's console to reprint it after an update
     */
    private void UpdateView() throws Exception {
        try {
            final String operatingSystem = System.getProperty("os.name");
            System.out.println("OS:" + operatingSystem);
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
