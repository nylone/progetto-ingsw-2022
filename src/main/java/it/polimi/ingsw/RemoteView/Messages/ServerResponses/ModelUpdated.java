package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.Model.ModelReader;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class ModelUpdated extends Response {
    @Serial
    private static final long serialVersionUID = 311L;

    private final ModelReader model;

    public ModelUpdated(ModelReader model) {
        super(StatusCode.Success);
        this.model = model;
    }

    public ModelReader getModel() {
        return this.model;
    }

}
