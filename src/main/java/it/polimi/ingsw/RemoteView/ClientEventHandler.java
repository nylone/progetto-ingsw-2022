package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.RemoteView.Messages.ClientEvents.ClientEvent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientEventHandler {
    private final BlockingQueue<ClientEvent> queue;
    private ClientEventListener eventListener;

    protected ClientEventHandler(ClientEventListener eventListener) {
        this.queue = new ArrayBlockingQueue<>(10);
        this.eventListener = eventListener;
    }

    protected void enqueue(ClientEvent event) throws InterruptedException {
        this.queue.put(event);
        new Thread(() -> {
            try {
                ClientEvent clientEvent = queue.take();
                eventListener.receive(clientEvent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    protected void changeListener(ClientEventListener eventListener) {
        this.eventListener = eventListener;
    }
}
