package it.polimi.ingsw.Network;

import it.polimi.ingsw.Server.Messages.HeartBeatMessage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * an auto sender of {@link HeartBeatMessage} at regular intervals
 */
public class HeartBeatSender extends TimerTask {
    private final SocketWrapper sw;
    private final Timer timer;

    /**
     * Creates the sender but does not activate it
     * @param sw the socket to send heartbeats on
     */
    public HeartBeatSender(SocketWrapper sw) {
        this.sw = sw;
        this.timer = new Timer();
    }

    /**
     * Activates the sender. The sender will stop running only if externally stopped or if the socketwrapper handling it
     * has issues during delivery of a heartbeat.
     * @param keepAlivePeriod the time, in milliseconds, between each {@link HeartBeatMessage} sent
     */
    public void start(long keepAlivePeriod) {
        this.timer.scheduleAtFixedRate(this, 0, keepAlivePeriod);
    }

    /**
     * sends a {@link HeartBeatMessage} over the wrapper. In case of errors during the sending of a message, closes the
     * wrapper and cancels the repeating task from the timer.
     */
    public void run() {
        try {
            sw.sendMessage(new HeartBeatMessage());
        } catch (IOException e) {
            try {
                sw.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            timer.cancel();
        }
    }
}
