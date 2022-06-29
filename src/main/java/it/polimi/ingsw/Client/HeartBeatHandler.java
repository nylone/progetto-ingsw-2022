package it.polimi.ingsw.Client;

import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Requests.HeartBeatRequest;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The server expects to receive a {@link HeartBeatRequest} at intervals of max 15 seconds. This Handler is responsible
 * for keeping the connection alive
 */
public class HeartBeatHandler {

    /**
     * Given a socket, keep the connection active
     *
     * @param socketWrapper the socket to keep alive
     */
    public static void handle(SocketWrapper socketWrapper) {
        Timer timer = new Timer();

        class HeartBeatTask extends TimerTask {
            public void run() {
                try {
                    socketWrapper.sendMessage(new HeartBeatRequest());
                } catch (IOException e) {
                    socketWrapper.close();
                    timer.cancel();
                }
            }
        }

        timer.scheduleAtFixedRate(new HeartBeatTask(), 0, 5000);
    }
}
