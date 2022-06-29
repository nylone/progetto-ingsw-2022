package it.polimi.ingsw.Server;

import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Internal.SocketClosedEvent;
import it.polimi.ingsw.Server.Messages.Events.Requests.ClientRequest;
import it.polimi.ingsw.Server.Messages.Message;

import java.io.IOException;

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
                if (message instanceof ClientRequest request) {
                    queue.enqueue(request);
                } else {
                    Logger.severe(
                            "Received unhandled Message that was not of type" + ClientRequest.class.getName() + ".\n");
                    return;
                }
            }
        } catch (IOException | InterruptedException e) {
            Logger.info("closing SocketListener");
            this.socket.close();
            try {
                queue.enqueue(new SocketClosedEvent());
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            Logger.info("closed SocketListener");
        }
    }
}
