package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

import java.util.List;

import static it.polimi.ingsw.Constants.CONTAINER_NAME_ENTRANCE;
import static it.polimi.ingsw.Constants.INPUT_NAME_CLOUD;

public class ChooseCloudTile extends PlayerAction {

    private final int selectedTile;

    public ChooseCloudTile(int playerBoardId, int selectedTile) {
        super(playerBoardId);
        this.selectedTile = selectedTile;
    }

    @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) throws InputValidationException {
        if (
                !(history.stream().
                        filter(playerAction -> playerAction.getClass() == MoveMotherNature.class)
                        .count() == 1)
        ) {
            throw new GenericInputValidationException("History", "MoveMotherNature action has not been executed");
        }

        if (!(history.get(history.size() - 1).getClass() == MoveMotherNature.class || (history.get(history.size() - 1).getClass() == PlayCharacterCard.class))) {
            throw new GenericInputValidationException("History", "This action can only be executed after a MoveMotherNature action or PlayCharacterCard action");
        }
        if (!(this.selectedTile >= 0 && selectedTile <= ctx.getClouds().size() - 1)) {
            throw new InvalidElementException(INPUT_NAME_CLOUD);
        }
        PlayerBoard caller = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        Cloud selectedCloud = ctx.getClouds().get(selectedTile);

        if (!(caller.getEntranceSpaceLeft() >= selectedCloud.getContents().size())) {
            throw new GenericInputValidationException(CONTAINER_NAME_ENTRANCE,
                    CONTAINER_NAME_ENTRANCE + "can't contain " + selectedCloud.getContents().size()
                            + " element's without overflowing.");
        }
        if (selectedCloud.getContents().size() == 0) {
            throw new GenericInputValidationException(CONTAINER_NAME_ENTRANCE,
                    CONTAINER_NAME_ENTRANCE + "has already been emptied");
        }
        if (!super.validate(history, ctx)) {
            throw new GenericInputValidationException("Action", "this action can't be executed more than once or be executed by other player than the current");
        }
        return true;
    }

    @Override
    protected void unsafeExecute(GameBoard ctx) {
        Cloud selectedCloud = ctx.getClouds().get(selectedTile); //get cloud
        try {
            ctx.getMutableTurnOrder()
                    .getMutableCurrentPlayer()
                    .addStudentsToEntrance(selectedCloud.extractContents());//fill playerboard's entrance
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
