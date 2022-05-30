package it.polimi.ingsw.Model;

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

    public List<IslandGroup> getIslandGroups() {
        return this.gameBoard.getMutableIslandField().getMutableGroups();
    }
    public List<PlayerBoard> getPlayerBoards() {
        return this.gameBoard.getMutablePlayerBoards();
    }
}
