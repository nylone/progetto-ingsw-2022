package it.polimi.ingsw.Network;

import java.util.Timer;
import java.util.TimerTask;

public class HeartBeatTimeoutTask extends TimerTask {
    private final Timer timer;
    private final SocketWrapper sock;

    public HeartBeatTimeoutTask(Timer timer, SocketWrapper sock) {
        this.timer = timer;
        this.sock = sock;
    }

    public void run() {
        sock.close();
        timer.cancel();
    }
}