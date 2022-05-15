package it.polimi.ingsw.RemoteView.Messages.Events.Requests;

import it.polimi.ingsw.Controller.Actions.PlayerAction;
import it.polimi.ingsw.RemoteView.Messages.Events.ClientEvent;
import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

import java.io.*;

public class PlayerActionRequest extends ClientEvent implements MessageBuilder {
    private final byte[] payload;

    public PlayerActionRequest(PlayerAction action) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream writer = new ObjectOutputStream(out);
        writer.writeObject(action);
        this.payload = out.toByteArray();
    }

    public PlayerAction getAction() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.payload);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (PlayerAction) objectInputStream.readObject();
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.REQUEST_PLAYER_ACTION;
    }
}
