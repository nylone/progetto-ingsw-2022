package it.polimi.ingsw.RemoteView.Messages.Events;

import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

import java.io.*;

public class PlayerAction extends ClientEvent implements MessageBuilder {
    private final byte[] payload;

    public PlayerAction(PlayerAction action) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream writer = new ObjectOutputStream(out);
        writer.writeObject(action);
        this.payload = out.toByteArray();
    }

    public it.polimi.ingsw.Controller.PlayerAction getAction() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.payload);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (it.polimi.ingsw.Controller.PlayerAction) objectInputStream.readObject();    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.REQUEST_START_GAME;
    }
}
