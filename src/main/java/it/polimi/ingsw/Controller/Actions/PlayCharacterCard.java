package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.CharacterCard;
import it.polimi.ingsw.Model.CharacterCardInput;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.GamePhase;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.PlayerBoard;

import java.io.Serial;
import java.util.List;

import static it.polimi.ingsw.Constants.*;

public class PlayCharacterCard extends PlayerAction {
    // todo why not use character card input? otherwise it's weird
    @Serial
    private static final long serialVersionUID = 207L; // convention: 2 for controller, (01 -> 99) for objects

    private final int selectedCard; // mandatory
    private final Optional<Integer> optTargetIsland;
    private final Optional<PawnColour> optTargetPawn;
    private final Optional<List<Pair<PawnColour, PawnColour>>> optTargetPawnPairs;

    public PlayCharacterCard(
            int playerBoardId,
            int selectedCard,
            Optional<Integer> optTargetIsland,
            Optional<PawnColour> optTargetPawn,
            Optional<List<Pair<PawnColour, PawnColour>>> optTargetPawnPairs) {
        super(playerBoardId, true);
        this.selectedCard = selectedCard;
        this.optTargetIsland = optTargetIsland;
        this.optTargetPawn = optTargetPawn;
        this.optTargetPawnPairs = optTargetPawnPairs;
    }

    /**
     * {@inheritDoc}
     * <ul>
     *     <li>The {@link GameMode} must be {@link GameMode#ADVANCED}</li>
     *     <li>The player must have played a {@link PlayAssistantCard} action before</li>
     *     <li>The selected character card must be within bounds (index 0 to 2 included)</li>
     *     <li>The player needs to have enough coins to by the card</li>
     *     <li>The {@link CharacterCardInput} generated from the attributes must be correct</li>
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
        if (ctx.getGameMode() == GameMode.SIMPLE) {
            return Optional.of(new GenericInputValidationException(INPUT_NAME_CHARACTER_CARD, INPUT_NAME_CHARACTER_CARD + " can't be played in simple mode"));
        }
        PlayerBoard caller = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        if (ctx.getMutableTurnOrder().getGamePhase() != GamePhase.ACTION) {
            return Optional.of(new GenericInputValidationException(HISTORY, "the game is not in the correct phase"));
        }

        // generate the input object before validation
        CharacterCardInput cardInput;
        try {
            cardInput = generateCharacterCardInput(caller, ctx);
        } catch (InvalidContainerIndexException e) {
            return Optional.of(new InvalidElementException(INPUT_NAME_TARGET_ISLAND));
        }

        if (!(this.selectedCard >= 0 && this.selectedCard < 3)) { //selectedCard out of bounds
            return Optional.of(new InvalidElementException(INPUT_NAME_CHARACTER_CARD));
        }
        CharacterCard selectedCard = ctx.getCharacterCards().get(this.selectedCard);
        if (caller.getCoinBalance() < selectedCard.getCost()) {
            return Optional.of(new GenericInputValidationException(INPUT_NAME_CHARACTER_CARD,
                    INPUT_NAME_CHARACTER_CARD + " can't be played due to insufficient coin balance"));
        }

        // todo redo character card validation
        try {
            selectedCard.checkInput(cardInput);
        } catch (InputValidationException e) {
            return Optional.of(e);
        }

        return Optional.empty();
    }

    @Override
    public void unsafeExecute(Model ctx) {
        PlayerBoard caller = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        CharacterCard characterCard = ctx.getCharacterCards().get(this.selectedCard);
        caller.payCharacterEffect(characterCard.getCost());
        if (characterCard.getTimeUsed() > 0) {
            ctx.addCoinToReserve(characterCard.getCost());
        } else {
            ctx.addCoinToReserve(characterCard.getCost() - 1); //the first time, one coin has to be placed on the card and not in the coin reserve
        }
        try {
            characterCard.unsafeUseCard(generateCharacterCardInput(caller, ctx));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CharacterCardInput generateCharacterCardInput(PlayerBoard caller, Model ctx) throws InvalidContainerIndexException {
        CharacterCardInput out = new CharacterCardInput(caller);
        if (this.optTargetIsland.isPresent()) {
            int id = this.optTargetIsland.get();
            out.setTargetIsland(ctx.getMutableIslandField().getMutableIslandById(id));
        }
        this.optTargetPawn.ifPresent(out::setTargetPawn);
        this.optTargetPawnPairs.ifPresent(out::setTargetPawnPairs);
        return out;
    }
}
