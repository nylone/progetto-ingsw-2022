package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ModelUpdated extends Response {
    private final byte[] model;

    public ModelUpdated(GameBoard model) throws IOException {
        super(StatusCode.Success);
        this.model = model.getSerializedModel();
    }

    public GameBoard getModel() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.model);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (GameBoard) objectInputStream.readObject();
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.RESPONSE_MODEL_UPDATED;
    }
}
