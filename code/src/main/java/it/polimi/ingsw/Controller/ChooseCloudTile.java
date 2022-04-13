package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.FullEntranceException;
import it.polimi.ingsw.Exceptions.NoPawnInCloudException;
import it.polimi.ingsw.Model.*;

import java.util.List;

public class ChooseCloudTile extends PlayerAction {

    private final int selectedTile;

    public ChooseCloudTile(int playerBoardId, int selectedTile) {
        super(playerBoardId);
        this.selectedTile = selectedTile;
    }

    @Override
    protected void execute(GameBoard ctx) {
        Cloud selectedCloud = ctx.getClouds().get(selectedTile); //get cloud
        try {
            ctx.getTurnOrder().getCurrent().addStudentsToEntrance(selectedCloud.extractContents());//fill playerboard's entrance
        } catch (FullEntranceException e) {
            e.printStackTrace();
        } catch (NoPawnInCloudException e) {
            e.printStackTrace();
        }
        //todo clouds should be filled at the end of turn
    }

    @Override
    protected boolean validate(List<PlayerAction> history, GameBoard ctx){
        return super.validate(history, ctx) &&
                selectedTile>=0 && selectedTile<= ctx.getClouds().size()-1 &&  //selected a consistent cloud
                ctx.getClouds().get(selectedTile).getContents().size()>0; //Selected cloud has not been already picked

    }
}
