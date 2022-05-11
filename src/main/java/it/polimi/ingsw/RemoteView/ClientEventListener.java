package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;

public interface ClientEventListener {
    void receive(ClientEvent event);
}
