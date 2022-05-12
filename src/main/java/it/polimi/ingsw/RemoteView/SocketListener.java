package it.polimi.ingsw.RemoteView;

import com.google.gson.Gson;
import it.polimi.ingsw.RemoteView.Messages.Events.*;
import it.polimi.ingsw.RemoteView.Messages.Message;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

import java.io.IOException;
import java.util.logging.Logger;

public class SocketListener implements Runnable {
    private final SocketWrapper socket;
    private final ClientEventHandler queue;

    private SocketListener(SocketWrapper socket, ClientEventHandler queue) {
        this.socket = socket;
        this.queue = queue;
    }

    public static void subscribe(SocketWrapper socket, ClientEventHandler queue) {
        SocketListener sl = new SocketListener(socket, queue);
        new Thread(sl).start();
    }

    public void run() {
        try {
            while (true) {
                Message message = socket.awaitMessage();
                if (message == null) { // the message can be null from invalid json or closed socket
                    if (socket.isClosed()) {
                        queue.enqueue(new SocketClosed());
                        return;
                    }
                    continue;
                }
                ClientEvent clientEvent;
                switch (message.getType()) {
                    case REQUEST_DECLARE_PLAYER ->
                            clientEvent = new Gson().fromJson(message.getData(), DeclarePlayer.class);
                    case REQUEST_CONNECT_LOBBY ->
                            clientEvent = new Gson().fromJson(message.getData(), ConnectLobby.class);
                    case REQUEST_CREATE_LOBBY ->
                            clientEvent = new Gson().fromJson(message.getData(), CreateLobby.class);
                    case REQUEST_START_GAME ->
                            clientEvent = new Gson().fromJson(message.getData(), StartGame.class);
                    case default -> {
                        Logger.getLogger(this.getClass().getName()).severe(
                                "Received unhandled " + PayloadType.class.getName() + ".\n" +
                                        "Received: " + message.getType());
                        return;
                    }
                }
                queue.enqueue(clientEvent);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("closing SocketListener");
            this.socket.close();
        }
    }
}
