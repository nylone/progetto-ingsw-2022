package it.polimi.ingsw.Server;

import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Internal.SocketClosedEvent;
import it.polimi.ingsw.Server.Messages.Events.Requests.ClientRequest;
import it.polimi.ingsw.Server.Messages.Events.Requests.HeartBeatRequest;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SocketListener implements Runnable {
    private final SocketWrapper socket;
    private final ClientEventHandler queue;
    private Timer heartBeatTimer;
    private SocketListener(SocketWrapper socket, ClientEventHandler queue) {
        this.socket = socket;
        this.queue = queue;
        this.heartBeatTimer = new Timer();
    }

    public static void subscribe(SocketWrapper socket, ClientEventHandler queue) {
        SocketListener sl = new SocketListener(socket, queue);
        new Thread(sl).start();
    }

    public void run() {
        class HeartBeatTask extends TimerTask {
            private final Timer timer;
            private final SocketWrapper sock;

            public HeartBeatTask(Timer timer, SocketWrapper sock) {
                this.timer = timer;
                this.sock = sock;
            }

            public void run() {
                timer.cancel();
                sock.close();
            }
        }

        this.heartBeatTimer.schedule(new HeartBeatTask(this.heartBeatTimer, this.socket), 15000);

        try {
            while (true) {
                Object message = socket.awaitMessage();
                if (message instanceof HeartBeatRequest ignored) {
                    this.heartBeatTimer.cancel();
                    this.heartBeatTimer = new Timer();
                    this.heartBeatTimer.schedule(new HeartBeatTask(this.heartBeatTimer, this.socket), 15000);
                }
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
        } finally {
            this.socket.close();
            try {
                queue.enqueue(new SocketClosedEvent());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.info("closed SocketListener");
        }
    }
}
