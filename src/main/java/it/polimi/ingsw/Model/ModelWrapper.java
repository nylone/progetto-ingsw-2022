package it.polimi.ingsw.Model;

import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Server.Lobby;
import it.polimi.ingsw.Server.Messages.Events.Internal.GameOverEvent;
import it.polimi.ingsw.Server.Messages.Events.Internal.ModelUpdateEvent;

/**
 * The model class is used by the controller to enact the game logic and (optionally) notifies the server (view)
 * whenever a meaningful change to the underlying data is carried out.
 */
public class ModelWrapper {
    private final Model model;
    private final Optional<Lobby> toNotify;

    public ModelWrapper(GameMode gameMode, Lobby lobby, String... playerNicknames) {
        this.model = new Model(gameMode, playerNicknames);
        this.toNotify = Optional.ofNullable(lobby);
        notifyLobby();
    }

    private void notifyLobby() {
        this.toNotify.ifPresent(lobby -> {
            lobby.notifyPlayers(new ModelUpdateEvent(this.modelCopy(true)));
            this.model.getWinners().ifPresent(winners ->
                    lobby.notifyPlayers(new GameOverEvent(winners.stream()
                            .map(PlayerBoard::getNickname)
                            .toList())));
        });
    }

    public Model modelCopy(boolean sanitize) {
        Model copy = model.copy();
        if (sanitize) {
            copy.getMutableStudentBag().removeContentReference();
        }
        return copy;
    }

    public ModelWrapper(Model model, Lobby lobby) {
        this.model = model;
        this.toNotify = Optional.ofNullable(lobby);
        notifyLobby();
    }

    public <T extends Exception> void editModel(ModelModifier<T> modelModifier) throws Exception {
        modelModifier.modifyModel(this.model);
        notifyLobby();
    }

    public interface ModelModifier<T extends Exception> {
        void modifyModel(Model model) throws T;
    }
}
