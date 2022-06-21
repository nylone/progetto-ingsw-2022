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
import static it.polimi.ingsw.Misc.Utils.countSimilarClassOccurrences;

public class ChooseCloudTile extends PlayerAction {

    @Serial
    private static final long serialVersionUID = 201L; // convention: 2 for controller, (01 -> 99) for objects
    private final int selectedTile;

    public ChooseCloudTile(int playerBoardId, int selectedTile) {
        super(playerBoardId, true);
        this.selectedTile = selectedTile;
    }

    /**
     * {@inheritDoc}
     * <ul>
     *     <li>This action can be called only after having called one and only one {@link MoveMotherNature} action</li>
     *     <li>The previous {@link PlayerAction}s must be either {@link MoveMotherNature} or {@link PlayCharacterCard}</li>
     *     <li>The distance declared to move must be within acceptable ranges</li>
     *     <li>The Player who calls the action must have enough space in its "entrance" field to allow for all pawns on the
     *          tile to be transferred</li>
     *     <li>The selected cloud tile must not be empty (unless no other non-empty cloud tiles are present)</li>
     * </ul>
     *
     * @param history the controller stores a {@link List} of previous {@link PlayerAction}s related to the player taking
     *                the current turn (at every new turn, the history is cleared).
     *                Some actions may use this {@link List} to check for duplicates.
     * @param ctx     a reference to {@link Model}. Some actions may use this reference to check for consistency between what
     *                the actions declares and what the Model offers.
     * @return An empty {@link Optional} in case of a successful validation. Otherwise the returned {@link Optional}
     * contains the related {@link InputValidationException}
     */
    @Override
    protected Optional<InputValidationException> customValidation(List<PlayerAction> history, Model ctx) {
        if (countSimilarClassOccurrences(MoveMotherNature.class, history) != 1) {
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
        if (ctx.getClouds().stream().anyMatch(cloud -> cloud.getContents().size() != 0)) {
            if (selectedCloud.getContents().size() == 0) {
                return Optional.of(new GenericInputValidationException(CONTAINER_NAME_CLOUD,
                        CONTAINER_NAME_CLOUD + " has already been emptied"));
            }
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
