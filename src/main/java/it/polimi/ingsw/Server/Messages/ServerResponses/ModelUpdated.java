package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Model.Model;

import java.io.Serial;

/**
 * This Response is generated upon an edit of the game's model. Therefore, it is {@link FixedStatusResponse}
 */
public class ModelUpdated extends FixedStatusResponse {
    @Serial
    private static final long serialVersionUID = 311L;

    private final Model model;

    /**
     * Prepare a response with the updated model
     * @param model the updated model to send
     */
    public ModelUpdated(Model model) {
        this.model = model;
    }

    /**
     * Get the model
     * @return the {@link Model} that was updated
     */
    public Model getModel() {
        return this.model;
    }

}
