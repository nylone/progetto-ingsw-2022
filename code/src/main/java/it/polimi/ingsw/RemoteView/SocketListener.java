package it.polimi.ingsw.RemoteView;

import com.google.gson.Gson;
import it.polimi.ingsw.RemoteView.Messages.ClientEvents.ConnectLobby;
import it.polimi.ingsw.RemoteView.Messages.ClientEvents.CreateLobby;
import it.polimi.ingsw.RemoteView.Messages.ClientEvents.DeclarePlayer;
import it.polimi.ingsw.RemoteView.Messages.Message;
import it.polimi.ingsw.RemoteView.Messages.ClientEvents.Event;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class SocketListener implements Runnable {
    private final SocketWrapper socket;
    private final BlockingQueue<Event> queue;
    private final Object toCloseMutex;
    private boolean toClose;

    private SocketListener(SocketWrapper socket, BlockingQueue<Event> queue) {
        this.socket = socket;
        this.queue = queue;
        this.toClose = false;
        this.toCloseMutex = new Object();
    }

    public void close() {
        synchronized (toCloseMutex) {
            this.toClose = true;
        }
    }

    public void run() {
        try {
            while (!toClose) {
                Message message = socket.awaitMessage();
                if (message == null) { // the message can be null from invalid json or closed socket
                    if (socket.isClosed()) {
                        // todo add event on queue to close lobby server
                        return;
                    }
                    continue;
                }
                Event event;
                switch (message.getType()) {
                    case REQUEST_DECLARE_PLAYER -> event = new Gson().fromJson(message.getData(), DeclarePlayer.class);
                    case REQUEST_CONNECT_LOBBY -> event = new Gson().fromJson(message.getData(), ConnectLobby.class);
                    case REQUEST_CREATE_LOBBY -> event = new Gson().fromJson(message.getData(), CreateLobby.class);
                    case default -> {
                        return;
                    }
                }
                queue.put(event);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.socket.close();
        }
    }

    public static void listenAndEcho(SocketWrapper socket, BlockingQueue<Event> queue) {
        SocketListener sl = new SocketListener(socket, queue);
        new Thread(sl).start();
    }
}
