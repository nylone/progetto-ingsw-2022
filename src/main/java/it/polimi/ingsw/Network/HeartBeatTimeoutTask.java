package it.polimi.ingsw.Network;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Timeout timer task waiting to close the socket in case it is not cancelled in time.
 */
public class HeartBeatTimeoutTask extends TimerTask {
    private final SocketWrapper sock;

    /**
     * Private constructor setting up the attributes for the timeout task
     *
     * @param sock the socket to close in case of timeout
     */
    private HeartBeatTimeoutTask(SocketWrapper sock) {
        this.sock = sock;
    }

    /**
     * Sets up a single, non repeating timer counting down to the timeout.
     *
     * @param socketWrapper   the socket to close if the timer runs out
     * @param timeoutDuration the timeout duration in milliseconds
     * @return the timer that is counting down the timeout, calling {@link Timer#cancel()} on it will prevent the
     * timeout task from running, effectively resetting the timeout.
     */
    public static Timer startAndGetTimer(SocketWrapper socketWrapper, long timeoutDuration) {
        Timer timeoutTimer = new Timer();
        timeoutTimer.schedule(new HeartBeatTimeoutTask(socketWrapper), timeoutDuration);
        return timeoutTimer;
    }

    public void run() {
        try {
            sock.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}