package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Model.Model;

import java.io.Serial;

public class ModelUpdated extends FixedStatusResponse {
    @Serial
    private static final long serialVersionUID = 311L;

    private final Model model;

    public ModelUpdated(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return this.model;
    }

}
