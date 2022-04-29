package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlayCharacterCardTest {

    @Test
    public void checkUse() throws Exception{
        GameHandler gh = initializeGameBoard(20,1,1);
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        while(true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if(!(gameBoard.getMutableTurnOrder().getSelectedCards().stream()
                    .anyMatch(selected -> selected.getPriority() == finalCard.getPriority()))) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        int initialReserve = gameBoard.getCoinReserve();
        int initialBalance = player.getCoinBalance();
        StatefulEffect card1 = (StatefulEffect) gameBoard.getCharacterCards().get(0);
        PlayCharacterCard playCharacterCard = new PlayCharacterCard(player.getId(),0, Optional.of(0),
                                                                    Optional.of((PawnColour) (card1).getState().get(0)),
                                                                            Optional.empty());

        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertTrue(player.getCoinBalance() == initialBalance - gameBoard.getCharacterCards().get(0).getCost() +1);
        assertTrue(gameBoard.getMutableIslandField().getMutableIslandById(0).getStudents().contains((PawnColour) (card1).getState().get(0)));
        assertTrue(gameBoard.getCoinReserve() == initialReserve + gameBoard.getCharacterCards().get(0).getCost()-2);
        //minus 2 because now card's cost has been increased by one so we keep one coin in the card and return ORIGINAL COST - 1 to the Reserve

        /*
        -------------------------------------------
                PlayCharacterCardTest Card02
         ------------------------------------------
         */
        gh = initializeGameBoard(40, 2, 2);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        card = Utils.random(player.getMutableAssistantCards());
        playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        while(true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if(!(gameBoard.getMutableTurnOrder().getSelectedCards().stream()
                    .anyMatch(selected -> selected.getPriority() == finalCard.getPriority()))) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = gameBoard.getCoinReserve();
        CharacterCard card2 =  gameBoard.getCharacterCards().get(0);
        playCharacterCard = new PlayCharacterCard(player.getId(),0, Optional.empty(), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        System.out.println(gameBoard.getCharacterCards().get(0));
        assertTrue(player.getCoinBalance() == initialBalance - card2.getCost()+1);
        assertTrue(gameBoard.getCoinReserve() == initialReserve + gameBoard.getCharacterCards().get(0).getCost()-2);
        assertTrue(gameBoard.getMutableEffects().isAlternativeTeacherAssignmentEnabled());


    }

    private GameHandler initializeGameBoard(int coinReserve, int coinPerPlayer, int card){
        StudentBag studentBag = new StudentBag(24);
        List<PlayerBoard> players = new ArrayList<>(2);
        players.add(new PlayerBoard(1,2,"Rouge", studentBag));
        players.add(new PlayerBoard(2,2,"Rampeo", studentBag));
        for(PlayerBoard playerBoard : players){
            playerBoard.setCoinBalance(coinPerPlayer);
        }
        List<Cloud> clouds = new ArrayList<>(2);
        clouds.add(new Cloud(1));
        clouds.add(new Cloud(2));
        List<CharacterCard> characterCards = new ArrayList<>();
        GameBoard gameBoard = new GameBoard(new IslandField(),GameMode.ADVANCED,studentBag, players, new EnumMap<>(PawnColour.class),
                new TeamMapper(players), new TurnOrder(players.toArray(new PlayerBoard[0])),new EffectTracker(), clouds,
                characterCards, coinReserve, coinPerPlayer);
        gameBoard.refillClouds();
        switch (card){
            case 1: characterCards.add(new Card01(gameBoard));
            case 2: characterCards.add(new Card02(gameBoard));
            case 3: characterCards.add(new Card03(gameBoard));
            case 4: characterCards.add(new Card04(gameBoard));
            case 5: characterCards.add(new Card05(gameBoard));
            case 6: characterCards.add(new Card06(gameBoard));
            case 7: characterCards.add(new Card07(gameBoard));
            case 8: characterCards.add(new Card08(gameBoard));
            case 9: characterCards.add(new Card09(gameBoard));
            case 10: characterCards.add(new Card10(gameBoard));
            case 11: characterCards.add(new Card11(gameBoard));
            case 12: characterCards.add(new Card12(gameBoard));
        }
        GameHandler gameHandler = new GameHandler(gameBoard, new ArrayList<>());
        return gameHandler;
    }

    @Test(expected = InputValidationException.class)
    public void CharacterCardIndexOutOfBound() throws Exception {
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayCharacterCard playAction = new PlayCharacterCard(player.getId(), 5, Optional.empty(), Optional.empty(), Optional.empty());
        gh.executeAction(playAction);
    }
    @Test
    public void duplicateCharacterCardAction() throws Exception{
        StudentBag studentBag = new StudentBag(24);
        List<PlayerBoard> players = new ArrayList<>(2);
        players.add(new PlayerBoard(1,2,"Rouge", studentBag));
        players.add(new PlayerBoard(2,2,"Rampeo", studentBag));
        List<Cloud> clouds = new ArrayList<>(2);
        clouds.add(new Cloud(1));
        clouds.add(new Cloud(2));
        List<CharacterCard> characterCards = new ArrayList<>();
        GameBoard gameBoard = new GameBoard(new IslandField(),GameMode.ADVANCED,studentBag, players, new EnumMap<>(PawnColour.class),
                new TeamMapper(players), new TurnOrder(players.toArray(new PlayerBoard[0])),new EffectTracker(), clouds,
                characterCards,20,1 );
        gameBoard.refillClouds();
        characterCards.add(new Card01(gameBoard));
        characterCards.add(new Card02(gameBoard));
        characterCards.add(new Card03(gameBoard));
        GameHandler gh = new GameHandler(gameBoard,new ArrayList<>());
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        while(true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if(!(gameBoard.getMutableTurnOrder().getSelectedCards().stream()
                    .anyMatch(selected -> selected.getPriority() == finalCard.getPriority()))) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        StatefulEffect card1 = (StatefulEffect) gameBoard.getCharacterCards().get(0);
        PlayCharacterCard playCharacterCard = new PlayCharacterCard(player.getId(),0, Optional.of(0),
                Optional.of((PawnColour) (card1).getState().get(0)),
                Optional.empty());

        gh.executeAction(playCharacterCard);
        try {
            gh.executeAction(playCharacterCard);
        }catch (GenericInputValidationException exception){
            assertEquals("An error occurred while validating: Action\n" +
                    "The error was: this action can't be executed more than once or be executed by other player than the current", exception.getMessage());
        }
    }
}
