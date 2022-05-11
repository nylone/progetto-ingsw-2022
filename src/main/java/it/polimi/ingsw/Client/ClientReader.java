package it.polimi.ingsw.Client;

import com.google.gson.Gson;
import it.polimi.ingsw.Client.CLI.CliWriter;
import it.polimi.ingsw.RemoteView.Messages.Message;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.LobbyRedirect;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.Response;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.StatusCode;
import it.polimi.ingsw.RemoteView.SocketWrapper;

import java.io.IOException;
import java.util.UUID;

public class ClientReader implements Runnable{
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
        while(true) {
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
            AnalyzeResponse(response);
        }

    }

    private void AnalyzeResponse(Message serverResponse){
        switch (serverResponse.getType()){
            case RESPONSE_LOBBY_ACCEPT -> System.out.println("User accepted");
            case RESPONSE_LOBBY_REDIRECT -> {
                Response response = new Gson().fromJson(serverResponse.getData(), LobbyRedirect.class);
                UUID id = ((LobbyRedirect) response).getLobbyID();
                if(response.getStatusCode() == StatusCode.Success){
                    System.out.println("Joined to lobby, id: "+ id);
                    clientView.setLobbyID(id);
                }else{
                    System.out.println("Something gone wrong, lobby not joined");
                }
            }


        }


    }
}
