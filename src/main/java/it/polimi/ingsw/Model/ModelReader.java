package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Model.Enums.GameMode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class ModelReader implements Serializable {
    @Serial
    private static final long serialVersionUID = 138L;
    private final GameBoard gameBoard;

    public ModelReader(GameBoard gameBoard, boolean sanitize) {
        if (sanitize) {
            GameBoard copy = gameBoard.copy();
            copy.getMutableStudentBag().removeContentReference();
            this.gameBoard = copy;
        } else {
            this.gameBoard = gameBoard;
        }
    }

    // todo remove and use other getters to proxy the model
    @Deprecated
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public GameMode getGameMode() {
        return gameBoard.getGameMode();
    }

    public List<CharacterCard> getCharacterCards() {
        return gameBoard.getCharacterCards();
    }

    public List<IslandGroup> getIslandGroups() {
        return this.gameBoard.getMutableIslandField().getMutableGroups();
    }

    public List<PlayerBoard> getPlayerBoards() {
        return this.gameBoard.getMutablePlayerBoards();
    }

    public PlayerBoard getPlayerBoardByNickname(String nickname) throws InvalidContainerIndexException {
        return this.gameBoard.getMutablePlayerBoardByNickname(nickname);
    }
}
