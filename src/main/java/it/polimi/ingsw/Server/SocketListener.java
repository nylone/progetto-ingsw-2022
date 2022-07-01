package it.polimi.ingsw.Server;

import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.ClientEvent;
import it.polimi.ingsw.Server.Messages.Events.Internal.SocketClosedEvent;
import it.polimi.ingsw.Server.Messages.Events.Requests.ClientRequest;
import it.polimi.ingsw.Server.Messages.Message;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Given a {@link SocketWrapper} and a {@link BlockingQueue<ClientEvent>}, moves only the {@link it.polimi.ingsw.Server.Messages.Events.ClientEvent}
 * received on the socket to the queue.
 */
public class SocketListener implements Runnable {
    private final SocketWrapper socket;
    private final BlockingQueue<ClientEvent> queue;

    /**
     * Construct the listener
     *
     * @param socket the {@link SocketWrapper} to poll messages from
     * @param queue  the {@link BlockingQueue<ClientEvent>} to push events to
     */
    private SocketListener(SocketWrapper socket, BlockingQueue<ClientEvent> queue) {
        this.socket = socket;
        this.queue = queue;
    }

    /**
     * Given a socket and a queue, generate a listener and put it to work
     *
     * @param socket the {@link SocketWrapper} to poll messages from
     * @param queue  the {@link BlockingQueue<ClientEvent>} to push events to
     */
    public static void subscribe(SocketWrapper socket, BlockingQueue<ClientEvent> queue) {
        SocketListener sl = new SocketListener(socket, queue);
        new Thread(sl).start();
    }

    /**
     * Listens on the {@link SocketWrapper} for messages, passes {@link Message}s implementing {@link ClientEvent} to the
     * {@link BlockingQueue<ClientEvent>} for the server to read from. <br>
     * Note: in case of read errors from the socket, the socket will be closed and the listener terminated.
     */
    public void run() {
        try {
            while (true) {
                Message message = socket.awaitMessage();
                if (message instanceof ClientRequest request) {
                    queue.put(request);
                } else {
                    Logger.severe(
                            "Received unhandled Message that was not of type" + ClientRequest.class.getName() + ".\n");
                    return;
                }
            }
        } catch (IOException | InterruptedException e) {
            Logger.info("closing SocketListener");
            try {
                this.socket.close();
                queue.put(new SocketClosedEvent());
            } catch (Exception ee) {
                throw new RuntimeException(ee);
            }
            Logger.info("closed SocketListener");
        }
    }
}
