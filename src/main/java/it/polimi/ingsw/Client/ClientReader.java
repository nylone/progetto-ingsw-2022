package it.polimi.ingsw.Client;

import com.google.gson.Gson;
import it.polimi.ingsw.Client.CLI.CliWriter;
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
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void AnalyzeResponse(Message serverResponse) throws IOException, ClassNotFoundException {
        System.out.println(serverResponse.getType());
        System.out.println("---------");
        switch (serverResponse.getType()){
            case RESPONSE_LOBBY_ACCEPT -> {
                LobbyAccept response = new Gson().fromJson(serverResponse.getData(), LobbyAccept.class);
                if (response.getStatusCode() == StatusCode.Success) {
                    this.clientView.setLogged(true);
                    System.out.println("User accepted\n");
                    System.out.println("Available open lobbies:");
                    response.getOpenLobbies().stream().forEach(uuidStringPair -> System.out.println("ID: " + uuidStringPair.getFirst() + " admin: " + uuidStringPair.getSecond()));
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
                clientView.setGameStarted(true);
            }

            case RESPONSE_MODEL_UPDATED -> {
                ModelUpdated modelUpdated = new Gson().fromJson(serverResponse.getData(), ModelUpdated.class);
                this.clientView.setGame(modelUpdated.getModel());
                System.out.println("MODEL UPDATED");
            }

            case RESPONSE_PLAYER_ACTION_FEEDBACK -> {
                PlayerActionFeedback response = new Gson().fromJson(serverResponse.getData(), PlayerActionFeedback.class);
                System.out.println(response.getReport());
            }

        }
    }

    /**
     * This method clears Client's console to reprint it after an update
     *
     */
    private void UpdateView(){
        try {
            final String operatingSystem = System.getProperty("os.name");

            if (operatingSystem.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        }catch (Exception e){
            System.out.println("Clear operation failed");
        }

        System.out.println(this.clientView);
    }
}
