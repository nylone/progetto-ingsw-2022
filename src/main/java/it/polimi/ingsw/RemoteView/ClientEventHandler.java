package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.RemoteView.Messages.ClientEvents.ClientEvent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientEventHandler {
    private final BlockingQueue<ClientEvent> queue;
    private final ClientEventListener eventListener;

    public ClientEventHandler(ClientEventListener eventListener) {
        this.queue = new ArrayBlockingQueue<>(2);
        this.eventListener = eventListener;
    }

    public void enqueue(ClientEvent event) throws InterruptedException {
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
}
