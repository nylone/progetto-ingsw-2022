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
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

import java.io.Serial;
import java.util.List;

import static it.polimi.ingsw.Constants.*;

public class PlayCharacterCard extends PlayerAction {
    @Serial
    private static final long serialVersionUID = 207L; // convention: 2 for controller, (01 -> 99) for objects

    private final int selectedCard; // mandatory
    private final Optional<Integer> optTargetIsland;
    private final Optional<PawnColour> optTargetPawn;
    private final Optional<Pair<PawnColour, PawnColour>[]> optTargetPawnPairs;

    public PlayCharacterCard(
            int playerBoardId,
            int selectedCard,
            Optional<Integer> optTargetIsland,
            Optional<PawnColour> optTargetPawn,
            Optional<Pair<PawnColour, PawnColour>[]> optTargetPawnPairs) {
        super(playerBoardId);
        this.selectedCard = selectedCard;
        this.optTargetIsland = optTargetIsland;
        this.optTargetPawn = optTargetPawn;
        this.optTargetPawnPairs = optTargetPawnPairs;
    }

    protected boolean validate(List<PlayerAction> history, GameBoard ctx) throws InputValidationException {
        if (ctx.getGameMode() == GameMode.SIMPLE) {
            throw new GenericInputValidationException(INPUT_NAME_CHARACTER_CARD, INPUT_NAME_CHARACTER_CARD + "can't be played in simple mode");
        }
        PlayerBoard caller = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        if (ctx.getMutableTurnOrder().getMutableSelectedCard(caller).isEmpty()) {
            throw new GenericInputValidationException(HISTORY, "No PlayAssistantCard has been played");
        }

        // generate the input object before validation
        CharacterCardInput cardInput;
        try {
            cardInput = generateCharacterCardInput(caller, ctx);
        } catch (InvalidContainerIndexException e) {
            throw new InvalidElementException(INPUT_NAME_TARGET_ISLAND);
        }

        if (!(this.selectedCard >= 0 && this.selectedCard < 3)) { //selectedCard out of bounds
            throw new InvalidElementException(INPUT_NAME_CHARACTER_CARD);
        }
        if (!super.validate(history, ctx)) {
            throw new GenericInputValidationException("Action", "this action can't be executed more than once or be executed by other player than the current");
        }
        CharacterCard selectedCard = ctx.getCharacterCards().get(this.selectedCard);
        System.out.println("SERVER_BALANCE:" + caller.getCoinBalance());
        if (caller.getCoinBalance() < selectedCard.getCost()) {
            throw new GenericInputValidationException(INPUT_NAME_CHARACTER_CARD,
                    INPUT_NAME_CHARACTER_CARD + " can't be played due to insufficient coin balance");
        }

        return selectedCard.checkInput(cardInput);
    }

    @Override
    protected void unsafeExecute(GameBoard ctx) {
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

    private CharacterCardInput generateCharacterCardInput(PlayerBoard caller, GameBoard ctx) throws InvalidContainerIndexException {
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
