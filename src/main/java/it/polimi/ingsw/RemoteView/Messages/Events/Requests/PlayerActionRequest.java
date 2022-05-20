package it.polimi.ingsw.RemoteView.Messages.Events.Requests;

import it.polimi.ingsw.Controller.Actions.PlayerAction;

import java.io.*;

public class PlayerActionRequest extends ClientRequest {
    @Serial
    private static final long serialVersionUID = 355L;
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
}
