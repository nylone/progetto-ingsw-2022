package it.polimi.ingsw.Network;

import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Server.Messages.HeartBeatMessage;
import it.polimi.ingsw.Server.Messages.Message;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A {@link SocketWrapper} that also takes care of sending or responding to {@link HeartBeatMessage}, keeping the connection
 * active so long as the endpoint proves to be active
 */
public class KeepAliveSocketWrapper extends SocketWrapper {
    private final BlockingQueue<Message> inputQueue;
    private final HeartBeatSender heartBeatSender;
    private final long keepAlivePeriod;
    private Timer heartBeatTimer;

    /**
     * Wraps the socket around a keep alive wrapper. The socket can be active or passive, the former means the socket
     * actively sends a {@link HeartBeatMessage} at constant intervals, the latter means the socket sends a {@link HeartBeatMessage}
     * for every {@link HeartBeatMessage} received. <br>
     * The sockets will automatically close after a timeout, the timeout is 3 times the fixed duration used by an active
     * wrapper to send its heartbeats. <br>
     * Note: server used wrappers are encouraged to use the passive wrapper while clients are encouraged to use the active version
     * instead. <br>
     * Note: Assuming both ends of the connection use this wrapper keeping at least one active wrapper either as the client or
     * server will keep the connection alive until an error occurs without the need to manually send the
     * heartbeat. The same can be said if both ends of the connection are
     * active wrappers. The same cannot be said if both ends are passive.
     * @param socket the socket to wrap around
     * @param keepAlivePeriod the time (in milliseconds) it takes for an active wrapper to send a heartbeat and a third of the
     *                        timeout required to close the connection and deem the endpoint unreachable
     * @param activeHeartBeat if set to true, makes the wrapper send a {@link HeartBeatMessage} every period, if set to false makes
     *                        the {@link HeartBeatMessage} be sent only in response to a {@link HeartBeatMessage} in input.
     * @throws IOException if any issues occur while wrapping the socket.
     */
    public KeepAliveSocketWrapper(Socket socket, long keepAlivePeriod, boolean activeHeartBeat) throws IOException {
        super(socket);
        this.inputQueue = new ArrayBlockingQueue<>(10);
        this.heartBeatSender = new HeartBeatSender(this);
        this.heartBeatTimer = new Timer();
        this.keepAlivePeriod = keepAlivePeriod;
        new Thread(() -> {
            while (true) {
                try {
                    Message input = super.awaitMessage();
                    if (input instanceof HeartBeatMessage) {
                        this.heartBeatTimer.cancel();
                        if (!activeHeartBeat) {
                            this.sendMessage(new HeartBeatMessage());
                        }
                        this.heartBeatTimer = HeartBeatTimeoutTask.startAndGetTimer(this, keepAlivePeriod*3);
                    } else {
                        inputQueue.put(input);
                    }
                } catch (IOException | InterruptedException e) {
                    try {
                        this.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    this.heartBeatSender.cancel();
                    Logger.info("closed KeepAliveSocketWrapper");
                    return;
                }
            }
        }).start();
        if (activeHeartBeat) {
            this.heartBeatSender.start(keepAlivePeriod);
        }
    }

    /**
     * Blocks until a message is read or the heartbeat timeout expires
     * @return the read message
     * @throws IOException if the timeout runs out or if an error is encountered while fetching a message
     */
    @Override
    public Message awaitMessage() throws IOException {
        try {
            while (true) {
                Message polledMessage = inputQueue.poll(keepAlivePeriod, TimeUnit.MILLISECONDS);
                if (polledMessage != null) {
                    return polledMessage;
                } else if (this.isClosed()) {
                    throw new SocketException("SocketWrapper is closed");
                }
            }
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }
}
