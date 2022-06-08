package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.PlayerBoard;

import java.io.Serial;
import java.util.List;

import static it.polimi.ingsw.Constants.*;

public class ChooseCloudTile extends PlayerAction {

    @Serial
    private static final long serialVersionUID = 201L; // convention: 2 for controller, (01 -> 99) for objects
    private final int selectedTile;

    public ChooseCloudTile(int playerBoardId, int selectedTile) {
        super(playerBoardId, true);
        this.selectedTile = selectedTile;
    }

    @Override
    protected Optional<InputValidationException> customValidation(List<PlayerAction> history, Model ctx) {
        if (
                !(history.stream().
                        filter(playerAction -> playerAction.getClass() == MoveMotherNature.class)
                        .count() == 1)
        ) {
            return Optional.of(new GenericInputValidationException("History", "MoveMotherNature action has not been executed"));
        }

        if (!(history.get(history.size() - 1).getClass() == MoveMotherNature.class || (history.get(history.size() - 1).getClass() == PlayCharacterCard.class))) {
            return Optional.of(new GenericInputValidationException("History", "This action can only be executed after a MoveMotherNature action or PlayCharacterCard action"));
        }
        if (!(this.selectedTile >= 0 && selectedTile <= ctx.getClouds().size() - 1)) {
            return Optional.of(new InvalidElementException(INPUT_NAME_CLOUD));
        }
        PlayerBoard caller = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        Cloud selectedCloud = ctx.getClouds().get(selectedTile);

        if (!(caller.getEntranceSpaceLeft() >= selectedCloud.getContents().size())) {
            return Optional.of(new GenericInputValidationException(CONTAINER_NAME_ENTRANCE,
                    CONTAINER_NAME_ENTRANCE + " can't contain " + selectedCloud.getContents().size()
                            + " element's without overflowing."));
        }
        if (selectedCloud.getContents().size() == 0) {
            return Optional.of(new GenericInputValidationException(CONTAINER_NAME_CLOUD,
                    CONTAINER_NAME_CLOUD + " has already been emptied"));
        }
        return Optional.empty();
    }

    @Override
    public void unsafeExecute(Model ctx) {
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
