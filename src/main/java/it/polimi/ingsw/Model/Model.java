package it.polimi.ingsw.Model;

import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.RemoteView.Lobby;
import it.polimi.ingsw.RemoteView.Messages.Events.Internal.ModelUpdateEvent;

public class Model {
    private final GameBoard gameBoard;
    private Optional<Lobby> toNotify;

    public Model(GameMode gameMode, String... playerNicknames) {
        this.gameBoard = new GameBoard(gameMode, playerNicknames);
        this.toNotify = Optional.empty();
    }

    public Model(GameBoard gameBoard) {
        this.toNotify = Optional.empty();
        this.gameBoard = gameBoard;
    }

    public void addModelUpdateListener(Lobby lobby) {
        this.toNotify = Optional.of(lobby);
        this.toNotify.get().notifyPlayers(new ModelUpdateEvent(new ModelReader(this.gameBoard, true)));
    }

    public <T extends Exception> void editModel(ModelModifier<T> modelModifier) throws Exception {
        modelModifier.modifyModel(this.gameBoard);
        this.toNotify.ifPresent(lobby -> lobby.notifyPlayers(
                new ModelUpdateEvent(new ModelReader(this.gameBoard, true))
        ));
    }

    public ModelReader readModel() {
        return new ModelReader(this.gameBoard, false);
    }

    public GameBoard debugGameBoardReference() {
        return this.gameBoard;
    }

    public interface ModelModifier<T extends Exception> {
        void modifyModel(GameBoard gameBoard) throws T;
    }
}
