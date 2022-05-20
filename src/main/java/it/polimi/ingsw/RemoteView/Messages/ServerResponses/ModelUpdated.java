package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;

public class ModelUpdated extends Response {
    @Serial
    private static final long serialVersionUID = 311L;

    private final GameBoard model;

    public ModelUpdated(GameBoard model) {
        super(StatusCode.Success);
        this.model = model;
    }

    public GameBoard getModel() {
        return this.model;
    }

}
