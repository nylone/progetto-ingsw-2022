package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.ClientRequest;
import it.polimi.ingsw.RemoteView.Messages.Events.Internal.SocketClosedEvent;

import java.io.IOException;
import java.util.logging.Logger;

public class SocketListener implements Runnable {
    private static final Logger log = Logger.getLogger(SocketListener.class.getName());

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
                if (socket.awaitMessage() instanceof ClientRequest request) {
                    queue.enqueue(request);
                } else {
                    log.severe(
                            "Received unhandled Message that was not of type" + ClientRequest.class.getName() + ".\n");
                    return;
                }
            }
        } catch (IOException | InterruptedException e) {
            log.info("closing SocketListener");
        } finally {
            this.socket.close();
            try {
                queue.enqueue(new SocketClosedEvent());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("closed SocketListener");
        }
    }
}
