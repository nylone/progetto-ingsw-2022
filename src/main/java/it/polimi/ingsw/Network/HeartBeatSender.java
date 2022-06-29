package it.polimi.ingsw.Network;

import it.polimi.ingsw.Server.Messages.HeartBeatMessage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class HeartBeatSender extends TimerTask {
    private final SocketWrapper sw;
    private final Timer timer;

    public HeartBeatSender(SocketWrapper sw) {
        this.sw = sw;
        this.timer = new Timer();
    }

    public void start(long keepAlivePeriod) {
        this.timer.scheduleAtFixedRate(this, 0, keepAlivePeriod);
    }

    public void run() {
        try {
            sw.sendMessage(new HeartBeatMessage());
        } catch (IOException e) {
            sw.close();
            timer.cancel();
        }
    }
}
