package it.polimi.ingsw.Model;

import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.RemoteView.Lobby;
import it.polimi.ingsw.RemoteView.Messages.Events.Internal.ModelUpdateEvent;

public class Model {
    private final GameBoard gameBoard;
    private Optional<Lobby> toNotify;

    public Model(Lobby remoteViewLobby, GameMode gameMode, String... playerNicknames) {
        this.toNotify = Optional.of(remoteViewLobby);
        this.gameBoard = new GameBoard(gameMode, playerNicknames);
    }

    public Model(GameMode gameMode, String... playerNicknames) {
        this.gameBoard = new GameBoard(gameMode, playerNicknames);
    }

    public Model(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void addEventListener(Lobby lobby) {
        this.toNotify = Optional.of(lobby);
    }

    public <T extends Exception> void modifyModel(ModelModifier<T> modelModifier) throws Exception {
        modelModifier.modifyModel(this.gameBoard);
        this.toNotify.ifPresent(lobby -> lobby.notifyPlayers(new ModelUpdateEvent(this.gameBoard.getModelCopy())));
    }

    public GameBoard accessModel() {
        return this.gameBoard;
    }

    public interface ModelModifier<T extends Exception> {
        void modifyModel(GameBoard gameBoard) throws T;
    }
}
