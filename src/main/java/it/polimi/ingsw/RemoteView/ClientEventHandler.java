package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.ClientRequest;

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
