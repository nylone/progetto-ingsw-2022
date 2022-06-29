package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Misc.SerializableOptional;
import it.polimi.ingsw.Model.CharacterCard;
import it.polimi.ingsw.Model.CharacterCardInput;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.GamePhase;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.PlayerBoard;

import java.io.Serial;
import java.util.List;


public class PlayCharacterCard extends PlayerAction {
    @Serial
    private static final long serialVersionUID = 207L; // convention: 2 for controller, (01 -> 99) for objects

    private final int selectedCard; // mandatory
    private final SerializableOptional<Integer> optTargetIsland;
    private final SerializableOptional<PawnColour> optTargetPawn;
    private final SerializableOptional<List<Pair<PawnColour, PawnColour>>> optTargetPawnPairs;

    /**
     * Create a new instance of this class with the following inputs:
     *
     * @param playerBoardId      the ID of the current {@link PlayerBoard}
     * @param selectedCard       the ID of the {@link CharacterCard} to be played
     * @param optTargetIsland    if a target island is required as an input to the card, provide it here
     * @param optTargetPawn      if a target pawn is required as an input to the card, provide it here
     * @param optTargetPawnPairs if a list of pawn pairs is required as an input to the card, provide it here
     */
    public PlayCharacterCard(
            int playerBoardId,
            int selectedCard,
            SerializableOptional<Integer> optTargetIsland,
            SerializableOptional<PawnColour> optTargetPawn,
            SerializableOptional<List<Pair<PawnColour, PawnColour>>> optTargetPawnPairs) {
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
     *     <li>The {@link GamePhase} must be {@link GamePhase#ACTION}</li>
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
     * @return An empty {@link SerializableOptional} in case of a successful validation. Otherwise the returned {@link SerializableOptional}
     * contains the related {@link InputValidationException}
     */
    @Override
    protected SerializableOptional<InputValidationException> customValidation(List<PlayerAction> history, Model ctx) {
        if (ctx.getGameMode() != GameMode.ADVANCED) {
            return SerializableOptional.of(new GenericInputValidationException("Character Card", "can't be played in simple mode"));
        }
        PlayerBoard caller = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        if (ctx.getMutableTurnOrder().getGamePhase() != GamePhase.ACTION) {
            return SerializableOptional.of(new GenericInputValidationException("History", "the game is not in the correct phase"));
        }

        // generate the input object before validation
        CharacterCardInput cardInput;
        try {
            cardInput = generateCharacterCardInput(caller, ctx);
        } catch (InvalidContainerIndexException e) {
            return SerializableOptional.of(new InvalidElementException("Target Island"));
        }

        if (!(this.selectedCard >= 0 && this.selectedCard < 3)) { //selectedCard out of bounds
            return SerializableOptional.of(new InvalidElementException("Character Card"));
        }
        CharacterCard selectedCard = ctx.getCharacterCards().get(this.selectedCard);
        if (caller.getCoinBalance() < selectedCard.getCost()) {
            return SerializableOptional.of(new GenericInputValidationException("Character Card",
                    "can't be played due to insufficient coin balance"));
        }

        try {
            selectedCard.checkInput(cardInput);
        } catch (InputValidationException e) {
            return SerializableOptional.of(e);
        }

        return SerializableOptional.empty();
    }

    @Override
    public void unsafeExecute(Model ctx) throws Exception {
        PlayerBoard caller = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        CharacterCard characterCard = ctx.getCharacterCards().get(this.selectedCard);
        caller.payCharacterEffect(characterCard);
        if (characterCard.getTimeUsed() > 0) {
            ctx.addCoinToReserve(characterCard.getCost());
        } else {
            ctx.addCoinToReserve(characterCard.getCost() - 1); //the first time, one coin has to be placed on the card and not in the coin reserve
        }
        characterCard.unsafeUseCard(generateCharacterCardInput(caller, ctx));
    }

    /**
     * a {@link CharacterCard} requires more information than the one contained in the constructor of this {@link PlayerAction}
     * to be able to function. During the calls to verify the action, a {@link CharacterCardInput} object needs to be created so
     * the internal validation mechanism of the {@link CharacterCard} can work.
     *
     * @param caller instead of the id of the current player, a reference to the current {@link PlayerBoard} is needed
     * @param ctx    the {@link Model} object is required to allow translation of the attributes of this object into a {@link CharacterCardInput}
     * @return the {@link CharacterCardInput} associated with this object's attributes
     * @throws InvalidContainerIndexException if the attribute of target island is pointing to an unidentified island-ID in the model, this exception is thrown
     */
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
