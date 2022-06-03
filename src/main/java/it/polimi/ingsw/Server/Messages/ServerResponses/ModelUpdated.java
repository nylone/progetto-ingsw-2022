package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class ModelUpdated extends Response {
    @Serial
    private static final long serialVersionUID = 311L;

    private final Model model;

    public ModelUpdated(Model model) {
        super(StatusCode.Success);
        this.model = model;
    }

    public Model getModel() {
        return this.model;
    }

}
