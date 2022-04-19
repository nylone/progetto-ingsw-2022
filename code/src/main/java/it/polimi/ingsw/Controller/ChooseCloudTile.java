package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.FullEntranceException;
import it.polimi.ingsw.Exceptions.NoPawnInCloudException;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.GameBoard;

import java.util.List;

public class ChooseCloudTile extends PlayerAction {

    private final int selectedTile;

    public ChooseCloudTile(int playerBoardId, int selectedTile) {
        super(playerBoardId);
        this.selectedTile = selectedTile;
    }

    @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) {
        return super.validate(history, ctx) &&
                selectedTile >= 0 && selectedTile <= ctx.getClouds().size() - 1 &&  //selected a consistent cloud
                ctx.getClouds().get(selectedTile).getContents().size() > 0; //Selected cloud has not been already picked

    }

    @Override
    protected void unsafeExecute(GameBoard ctx) {
        Cloud selectedCloud = ctx.getClouds().get(selectedTile); //get cloud
        try {
            ctx.getTurnOrder().getCurrentPlayer().addStudentsToEntrance(selectedCloud.extractContents());//fill playerboard's entrance
        } catch (FullEntranceException | NoPawnInCloudException e) {
            e.printStackTrace();
        }
    }
}
