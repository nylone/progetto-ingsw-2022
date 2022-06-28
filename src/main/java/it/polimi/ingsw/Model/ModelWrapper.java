package it.polimi.ingsw.Model;

import it.polimi.ingsw.Misc.SerializableOptional;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Server.Lobby;
import it.polimi.ingsw.Server.Messages.Events.Internal.GameOverEvent;
import it.polimi.ingsw.Server.Messages.Events.Internal.ModelUpdateEvent;

import java.util.Objects;

/**
 * This class is used by the controller to enact the game logic and (optionally) notifies the view (ie the server)
 * whenever a meaningful change to the underlying data is carried out.
 */
public class ModelWrapper {
    private Model model;
    private final SerializableOptional<Lobby> toNotify;

    /**
     * Wraps a {@link Model} along with a {@link SerializableOptional}<{@link Lobby}> object to allow for easy notification
     * to the view (ie the lobby component) of any and all changes to the model that are carried through this object's method:
     * {@link #editModel(ModelModifier, boolean)}
     * @param model a non null reference to the Model
     * @param lobby a non null optional value (can obviously be empty, but not null)
     */
    public ModelWrapper(Model model, SerializableOptional<Lobby> lobby) {
        Objects.requireNonNull(model);
        Objects.requireNonNull(lobby);

        this.model = model;
        this.toNotify = lobby;
        notifyLobby();
    }

    /**
     * When called, notifies all clients connected to the lobby of a {@link ModelUpdateEvent} and also (if necessary)
     * of a {@link GameOverEvent}
     */
    private void notifyLobby() {
        this.toNotify.ifPresent(lobby -> {
            lobby.notifyPlayers(new ModelUpdateEvent(this.modelCopy(true)));
            this.model.getWinners().ifPresent(winners ->
                    lobby.notifyPlayers(new GameOverEvent(winners.stream()
                            .map(PlayerBoard::getNickname)
                            .toList())));
        });
    }

    /**
     * When called, returns a copy of the Model object
     * @param sanitize if set to true, tells the method to remove the {@link StudentBag} reference to prevent
     *                 peeking at the contents of the bag
     * @return an optionally sanitized clone of the wrapped {@link Model} object
     */
    public Model modelCopy(boolean sanitize) {
        Model copy = model.copy();
        if (sanitize) {
            copy.getMutableStudentBag().removeContentReference();
        }
        return copy;
    }

    /**
     * When called, allows a {@link ModelModifier} type of function to carry out changes to the {@link Model}, then notifies
     * the lobby of such changes
     * @param modelModifier a function or method that can be linked to the {@link ModelModifier} interface
     * @param keepUnsafeReference if set to true, the model reference is kept unaltered after a successful edit action, allowing for
     *                            debugging introspection of the model. If unsure, set it to false for best security.
     * @throws Exception the modelModifier can optionally throw Exceptions, which will be escalated to the caller.
     */
    public void editModel(ModelModifier modelModifier, boolean keepUnsafeReference) throws Exception {
        Model toModify;
        if (keepUnsafeReference) {
            toModify = this.model;
        } else {
            toModify= modelCopy(false);
        }
        modelModifier.modifyModel(toModify);
        this.model = toModify;
        notifyLobby();
    }

    /**
     * An interface that covers methods trying to access the Model. Read {@link #modifyModel(Model)} for more information
     */
    public interface ModelModifier {
        /**
         * The function responsible for changes to the {@link Model}
         * @param model a reference to the {@link Model} object. In order to grant safe access to the model, the reference ceases to hold
         *              meaning once this function terminates
         * @throws Exception the modelModifier can optionally throw Exceptions, which will be escalated to the caller.
         */
        void modifyModel(Model model) throws Exception;
    }
}
