package it.polimi.ingsw.Network;

import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Server.Messages.HeartBeatMessage;
import it.polimi.ingsw.Server.Messages.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class KeepAliveSocketWrapper extends SocketWrapper {
    private final BlockingQueue<Message> inputQueue;
    private final HeartBeatSender heartBeatSender;
    private final long keepAlivePeriod;
    private Timer heartBeatTimer;

    public KeepAliveSocketWrapper(Socket socket, long keepAlivePeriod, boolean activeHeartBeat) throws IOException {
        super(socket);
        this.inputQueue = new ArrayBlockingQueue<>(10);
        this.heartBeatSender = new HeartBeatSender(this);
        this.heartBeatTimer = new Timer();
        this.keepAlivePeriod = keepAlivePeriod;
        new Thread(() -> {
            this.heartBeatTimer.schedule(new HeartBeatTimeoutTask(this.heartBeatTimer, this), keepAlivePeriod * 3);
            while (true) {
                try {
                    Message input = super.awaitMessage();
                    if (input instanceof HeartBeatMessage) {
                        this.heartBeatTimer.cancel();
                        if (!activeHeartBeat) {
                            this.sendMessage(new HeartBeatMessage());
                        }
                        this.heartBeatTimer = new Timer();
                        this.heartBeatTimer.schedule(new HeartBeatTimeoutTask(this.heartBeatTimer, this), keepAlivePeriod * 3);
                    } else {
                        inputQueue.put(input);
                    }
                } catch (IOException | InterruptedException e) {
                    this.close();
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

    @Override
    public Message awaitMessage() throws IOException {
        try {
            while (true) {
                Message polledMessage = inputQueue.poll(keepAlivePeriod, TimeUnit.MILLISECONDS);
                if (polledMessage != null) {
                    return polledMessage;
                } else if (this.isClosed()) {
                    throw new IOException();
                }
            }
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }
}
