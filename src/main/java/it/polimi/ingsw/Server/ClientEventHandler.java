package it.polimi.ingsw.Server;

import it.polimi.ingsw.Server.Messages.Events.ClientEvent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientEventHandler {
    private final BlockingQueue<ClientEvent> queue;

    protected ClientEventHandler() {
        this.queue = new ArrayBlockingQueue<>(10);
    }

    protected void enqueue(ClientEvent event) throws InterruptedException {
        this.queue.put(event);
    }

    protected ClientEvent dequeue() throws InterruptedException {
        return this.queue.take();
    }

}