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
                characterCards );
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
        System.out.println(card1.getState());
        PlayCharacterCard playCharacterCard = new PlayCharacterCard(player.getId(),0, Optional.of(0),
                                                                    Optional.of((PawnColour) (card1).getState().get(0)),
                                                                            Optional.empty());

        gh.executeAction(playCharacterCard);
        assertTrue(card1.getState().size() == 4);

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
                characterCards );
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
