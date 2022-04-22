package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.toremove.FullEntranceException;
import it.polimi.ingsw.Exceptions.toremove.NoPawnInCloudException;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

import java.util.List;

public class ChooseCloudTile extends PlayerAction {

    private final int selectedTile;

    public ChooseCloudTile(int playerBoardId, int selectedTile) {
        super(playerBoardId);
        this.selectedTile = selectedTile;
    }

    @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx) {
        PlayerBoard caller = ctx.getTurnOrder().getCurrentPlayer();
        Cloud selectedCloud = ctx.getClouds().get(selectedTile);

        return super.validate(history, ctx) &&
                caller.getEntranceStudents().size() + selectedCloud.getContents().size() <= caller.getEntranceSize() && //students don't exceed the entranceSize
                selectedTile >= 0 && selectedTile <= ctx.getClouds().size() - 1 &&  //selected a consistent cloud
                selectedCloud.getContents().size() > 0 && //Selected cloud has not been already picked
                ctx.getTurnOrder().getCurrentPlayer().getEntranceSpaceLeft() >= selectedCloud.getContents().size();
                // check that entrance is not full
    }

    @Override
    protected void unsafeExecute(GameBoard ctx) {
        Cloud selectedCloud = ctx.getClouds().get(selectedTile); //get cloud
        try {
            ctx.getTurnOrder().getCurrentPlayer().addStudentsToEntrance(selectedCloud.extractContents());//fill playerboard's entrance
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
