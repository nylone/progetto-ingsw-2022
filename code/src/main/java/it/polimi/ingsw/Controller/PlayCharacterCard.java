package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.CharacterCard;
import it.polimi.ingsw.Model.CharacterCardInput;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

import java.util.List;


import static it.polimi.ingsw.Constants.INPUT_NAME_CHARACTER_CARD;

public class PlayCharacterCard extends PlayerAction {

    private final int selectedCard; //mandatory
    private Integer targetIsland; //optional
    private PawnColour targetPawn; //optional
    private Pair<PawnColour, PawnColour>[] targetPawnPairs;  //optional

    private CharacterCardInput input;

    public PlayCharacterCard(PlayCharacterCardBuilder builder) {
        super(builder.playerBoardId);
        this.selectedCard = builder.selectedCard;
        this.targetIsland = builder.targetIsland;
        this.targetPawn = builder.targetPawn;
        this.targetPawnPairs = builder.targetPawnPairs;
    }

    @Override
    protected void unsafeExecute(GameBoard ctx) {
        PlayerBoard caller = ctx.getTurnOrder().getCurrentPlayer();
        CharacterCard characterCard = ctx.getCharacterCards().get(this.selectedCard);
        caller.PayCharacterEffect(characterCard.getCost());
        if (characterCard.getTimeUsed() > 0) {
            ctx.addToCoinReserve(characterCard.getCost());
        } else {
            ctx.addToCoinReserve(characterCard.getCost() - 1); //the first time, one coin has to be placed on the card and not in the coin reserve
        }
        characterCard.unsafeUseCard(input);
    }

    protected boolean validate(List<PlayerAction> history, GameBoard ctx) throws InputValidationException {
        PlayerBoard caller = ctx.getTurnOrder().getCurrentPlayer();
        try {
            CreateCharacterCardInput(caller, ctx);
        } catch (InvalidContainerIndexException e) {
            e.printStackTrace();
        }

        if(!(this.selectedCard>=0&&this.selectedCard<3)){ //selectedCard out of bounds
            throw new InvalidElementException(INPUT_NAME_CHARACTER_CARD);
        }
        if(!super.validate(history,ctx)){
            throw new GenericInputValidationException("Action", "this action can't be executed more than once or be executed by other player than the current");
        }
        CharacterCard selectedCard = ctx.getCharacterCards().get(this.selectedCard);
        if(caller.getCoinBalance() < selectedCard.getCost()){
            throw new GenericInputValidationException(INPUT_NAME_CHARACTER_CARD, INPUT_NAME_CHARACTER_CARD + "can't be played due to low coins balance");
        }
        if(!selectedCard.checkInput(input)){

        }
        return true;
    }

    private void CreateCharacterCardInput(PlayerBoard caller, GameBoard ctx) throws InvalidContainerIndexException {
        this.input = new CharacterCardInput(caller);
        this.input.setTargetIsland(this.targetIsland == null? null : ctx.getIslandField().getIslandById(this.targetIsland.intValue()));
        this.input.setTargetPawn(this.targetPawn == null? null : this.targetPawn);
        this.input.setTargetPawnPairs(this.targetPawnPairs == null ? null : this.targetPawnPairs);
    }

    //BUILDER PATTERN

    public static class PlayCharacterCardBuilder{
        private final int selectedCard; //mandatory
        private final int playerBoardId; //mandatory
        private Integer targetIsland; //optional
        private PawnColour targetPawn; //optional
        private Pair<PawnColour, PawnColour>[] targetPawnPairs;  //optional

        public PlayCharacterCardBuilder(int playerBoardId, int selectedCard){
            this.playerBoardId = playerBoardId;
            this.selectedCard = selectedCard;
        }

        public PlayCharacterCardBuilder targetIsland(Integer targetIsland){
            this.targetIsland = targetIsland;
            return this;
        }

        public PlayCharacterCardBuilder targetPawn(PawnColour targetPawn){
            this.targetPawn = targetPawn;
            return this;
        }

        public PlayCharacterCardBuilder targetPawnPairs(Pair<PawnColour, PawnColour>[] targetPawnPairs){
            this.targetPawnPairs = targetPawnPairs;
            return this;
        }

        public PlayCharacterCard build(){
            PlayCharacterCard playCharacterCard = new PlayCharacterCard(this);
            return playCharacterCard;
        }
    }
}
