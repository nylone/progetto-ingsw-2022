package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.MoveStudent;
import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Controller.Actions.PlayCharacterCard;
import it.polimi.ingsw.Controller.Enums.MoveDestination;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlayCharacterCardTest {

    @Test
    public void checkUse() throws Exception {
        GameHandler gh = initializeGameBoard(20, 1, 1);
        GameBoard gameBoard = playAssistantCard(gh).getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        int initialReserve = gameBoard.getCoinReserve();
        int initialBalance = player.getCoinBalance();
        StatefulEffect cardStateful = (StatefulEffect) gameBoard.getCharacterCards().get(0);
        PlayCharacterCard playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.of(0),
                Optional.of((PawnColour) (cardStateful).getState().get(0)),
                Optional.empty());

        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - gameBoard.getCharacterCards().get(0).getCost() + 1);
        assertTrue(gameBoard.getMutableIslandField().getMutableIslandById(0).getStudents().contains((PawnColour) (cardStateful).getState().get(0)));
        assertEquals(gameBoard.getCoinReserve(), initialReserve + gameBoard.getCharacterCards().get(0).getCost() - 2);
        //minus 2 because now card's cost has been increased by one so we keep one coin in the card and return ORIGINAL COST - 1 to the Reserve

        /*
        -------------------------------------------
                PlayCharacterCardTest Card02
         ------------------------------------------
         */
        gh = initializeGameBoard(40, 2, 2);
        gameBoard = playAssistantCard(gh).getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = gameBoard.getCoinReserve();
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - gameBoard.getCharacterCards().get(0).getCost() + 1);
        assertEquals(gameBoard.getCoinReserve(), initialReserve + gameBoard.getCharacterCards().get(0).getCost() - 2);
        assertTrue(gameBoard.getMutableEffects().isAlternativeTeacherAssignmentEnabled());

        /*
        -------------------------------------------
                PlayCharacterCardTest Card03
         ------------------------------------------
         */
        gh = initializeGameBoard(60, 3, 3);
        gameBoard = playAssistantCard(gh).getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = gameBoard.getCoinReserve();
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.of(0), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        MoveDestination moveDestination = MoveDestination.toIsland(0);
        MoveStudent moveStudent = new MoveStudent(player.getId(), 0, moveDestination);
        gh.executeAction(moveStudent);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        gh.executeAction(moveStudent);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 2, moveDestination);
        gh.executeAction(moveStudent);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - gameBoard.getCharacterCards().get(0).getCost() + 1);
        assertEquals(gameBoard.getCoinReserve(), initialReserve + gameBoard.getCharacterCards().get(0).getCost() - 2);
        assertTrue(gameBoard.getInfluencerOf(gameBoard.getMutableIslandField().getMutableGroups().get(0)).isEmpty() ||
                gameBoard.getInfluencerOf(gameBoard.getMutableIslandField().getMutableGroups().get(0)).get().equals(gameBoard.getTeamMapper().getTeamID(player)));

         /*
        -------------------------------------------
                PlayCharacterCardTest Card04
         ------------------------------------------
         */
        gh = initializeGameBoard(20, 1, 4);
        gameBoard = playAssistantCard(gh).getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = gameBoard.getCoinReserve();
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - gameBoard.getCharacterCards().get(0).getCost() + 1);
        assertEquals(gameBoard.getCoinReserve(), initialReserve + gameBoard.getCharacterCards().get(0).getCost() - 2);
        assertTrue(gameBoard.getMutableEffects().isMotherNatureMovementIncreased());

         /*
        -------------------------------------------
                PlayCharacterCardTest Card05
         ------------------------------------------
         */
        gh = initializeGameBoard(40, 2, 5);
        gameBoard = playAssistantCard(gh).getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = gameBoard.getCoinReserve();
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.of(0), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - gameBoard.getCharacterCards().get(0).getCost() + 1);
        assertEquals(gameBoard.getCoinReserve(), initialReserve + gameBoard.getCharacterCards().get(0).getCost() - 2);
        cardStateful = (StatefulEffect) gameBoard.getCharacterCards().get(0);
        assertEquals(3, cardStateful.getState().size()); // 3 tiles left after one use
        assertEquals(1, gameBoard.getMutableIslandField().getMutableIslandGroupById(0).getMutableNoEntryTiles().size()); // the island group contains the NoEntryTile

         /*
        -------------------------------------------
                PlayCharacterCardTest Card06
         ------------------------------------------
         */
        gh = initializeGameBoard(60, 3, 6);
        gameBoard = playAssistantCard(gh).getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = gameBoard.getCoinReserve();
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - gameBoard.getCharacterCards().get(0).getCost() + 1);
        assertEquals(gameBoard.getCoinReserve(), initialReserve + gameBoard.getCharacterCards().get(0).getCost() - 2);
        assertTrue(gameBoard.getMutableEffects().isTowerInfluenceDenied());
        /*
        -------------------------------------------
                PlayCharacterCardTest Card07
         ------------------------------------------
         */
        gh = initializeGameBoard(20, 1, 7);
        gameBoard = playAssistantCard(gh).getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = gameBoard.getCoinReserve();
        initialBalance = player.getCoinBalance();
        cardStateful = (StatefulEffect) gameBoard.getCharacterCards().get(0);
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(player.getEntranceStudents().get(0).get(), (PawnColour) cardStateful.getState().get(0)));
        pairs.add(new Pair<>(player.getEntranceStudents().get(1).get(), (PawnColour) cardStateful.getState().get(1)));
        Pair<PawnColour, PawnColour>[] pairsArray = new Pair[pairs.size()];
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.of(pairs.toArray(pairsArray)));
        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - gameBoard.getCharacterCards().get(0).getCost() + 1);
        assertEquals(gameBoard.getCoinReserve(), initialReserve + gameBoard.getCharacterCards().get(0).getCost() - 2);
        cardStateful = (StatefulEffect) gameBoard.getCharacterCards().get(0);
        assertTrue(cardStateful.getState().containsAll(pairs.stream().map(Pair::getFirst).toList()));
        assertTrue(player.getEntranceStudents().containsAll(pairs.stream().map(p -> Optional.of(p.getSecond())).toList()));

         /*
        -------------------------------------------
                PlayCharacterCardTest Card08
         ------------------------------------------
         */
        gh = initializeGameBoard(40, 2, 8);
        gameBoard = playAssistantCard(gh).getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = gameBoard.getCoinReserve();
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - gameBoard.getCharacterCards().get(0).getCost() + 1);
        assertEquals(gameBoard.getCoinReserve(), initialReserve + gameBoard.getCharacterCards().get(0).getCost() - 2);
        assertTrue(gameBoard.getMutableEffects().isInfluenceIncreased());

         /*
        -------------------------------------------
                PlayCharacterCardTest Card09
         ------------------------------------------
         */
        gh = initializeGameBoard(60, 3, 9);
        gameBoard = playAssistantCard(gh).getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = gameBoard.getCoinReserve();
        initialBalance = player.getCoinBalance();
        PawnColour pawnColour = Utils.random(Arrays.asList(PawnColour.BLUE, PawnColour.RED, PawnColour.YELLOW, PawnColour.GREEN, PawnColour.PINK));
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.of(pawnColour), Optional.empty());
        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - gameBoard.getCharacterCards().get(0).getCost() + 1);
        assertEquals(gameBoard.getCoinReserve(), initialReserve + gameBoard.getCharacterCards().get(0).getCost() - 2);
        assertTrue(gameBoard.getMutableEffects().isPawnColourDenied() && gameBoard.getMutableEffects().getDeniedPawnColour().get().equals(pawnColour));

         /*
        -------------------------------------------
                PlayCharacterCardTest Card10
         ------------------------------------------
         */
        gh = initializeGameBoard(20, 2, 10);
        gameBoard = playAssistantCard(gh).getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = gameBoard.getCoinReserve();
        initialBalance = player.getCoinBalance();
        PawnColour firstPawnDiningRoom = player.getEntranceStudents().get(0).get();
        PawnColour secondPawnDiningRoom = player.getEntranceStudents().get(1).get();
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 0, moveDestination);
        gh.executeAction(moveStudent);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        gh.executeAction(moveStudent);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        moveDestination = MoveDestination.toIsland(1);
        moveStudent = new MoveStudent(player.getId(), 2, moveDestination);
        gh.executeAction(moveStudent);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        pairs = new ArrayList<>();
        pairs.add(new Pair<>(player.getEntranceStudents().get(4).get(), firstPawnDiningRoom));
        pairs.add(new Pair<>(player.getEntranceStudents().get(5).get(), secondPawnDiningRoom));
        pairsArray = new Pair[pairs.size()];
        int firstCount = player.getDiningRoomCount(pairs.get(0).getFirst());
        int secondCount = player.getDiningRoomCount(pairs.get(1).getFirst());
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.of(pairs.toArray(pairsArray)));
        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - gameBoard.getCharacterCards().get(0).getCost() + 1);
        assertEquals(gameBoard.getCoinReserve(), initialReserve + gameBoard.getCharacterCards().get(0).getCost() - 2);
        assertTrue(player.getDiningRoomCount(pairs.get(0).getFirst()) >= firstCount - 2 && player.getDiningRoomCount(pairs.get(0).getFirst()) <= firstCount + 2); //equals 2 if students taken from entrance have the same colour
        assertTrue(player.getDiningRoomCount(pairs.get(1).getFirst()) >= secondCount - 2 && player.getDiningRoomCount(pairs.get(1).getFirst()) <= secondCount + 2);
        assertTrue(player.getEntranceStudents().containsAll(pairs.stream().map(p -> Optional.of(p.getSecond())).toList()));

        /*
        -------------------------------------------
                PlayCharacterCardTest Card11
         ------------------------------------------
         */

        gh = initializeGameBoard(40, 2, 11);
        gameBoard = playAssistantCard(gh).getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = gameBoard.getCoinReserve();
        initialBalance = player.getCoinBalance();
        cardStateful = (StatefulEffect) gameBoard.getCharacterCards().get(0);
        PawnColour chosenPawn = (PawnColour) cardStateful.getState().get(0);
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.of(chosenPawn), Optional.empty());
        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - gameBoard.getCharacterCards().get(0).getCost() + 1);
        assertEquals(gameBoard.getCoinReserve(), initialReserve + gameBoard.getCharacterCards().get(0).getCost() - 2);
        assertEquals(1, player.getDiningRoomCount(chosenPawn));
        assertEquals(4, cardStateful.getState().size());

        /*
        -------------------------------------------
                PlayCharacterCardTest Card12
         ------------------------------------------
         */
        gh = initializeGameBoard(60, 3, 12);
        gameBoard = playAssistantCard(gh).getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = gameBoard.getCoinReserve();
        initialBalance = player.getCoinBalance();
        chosenPawn = player.getEntranceStudents().get(1).get();
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        gh.executeAction(moveStudent);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.of(chosenPawn), Optional.empty());
        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - gameBoard.getCharacterCards().get(0).getCost() + 1);
        assertEquals(gameBoard.getCoinReserve(), initialReserve + gameBoard.getCharacterCards().get(0).getCost() - 2);
        assertEquals(0, player.getDiningRoomCount(chosenPawn));


    }

    private GameHandler initializeGameBoard(int coinReserve, int coinPerPlayer, int card) {
        StudentBag studentBag = new StudentBag(24);
        List<PlayerBoard> players = new ArrayList<>(2);
        players.add(new PlayerBoard(1, 2, "Rouge", studentBag));
        players.add(new PlayerBoard(2, 2, "Rampeo", studentBag));
        for (PlayerBoard playerBoard : players) {
            playerBoard.setCoinBalance(coinPerPlayer);
        }
        List<Cloud> clouds = new ArrayList<>(2);
        clouds.add(new Cloud(1));
        clouds.add(new Cloud(2));
        List<CharacterCard> characterCards = new ArrayList<>();
        GameBoard gameBoard = new GameBoard(new IslandField(), GameMode.ADVANCED, studentBag, players, new EnumMap<>(PawnColour.class),
                new TeamMapper(players), new TurnOrder(players.toArray(new PlayerBoard[0])), new EffectTracker(), clouds,
                characterCards, coinReserve, coinPerPlayer);
        gameBoard.refillClouds();
        switch (card) {
            case 1:
                characterCards.add(new Card01(gameBoard));
            case 2:
                characterCards.add(new Card02(gameBoard));
            case 3:
                characterCards.add(new Card03(gameBoard));
            case 4:
                characterCards.add(new Card04(gameBoard));
            case 5:
                characterCards.add(new Card05(gameBoard));
            case 6:
                characterCards.add(new Card06(gameBoard));
            case 7:
                characterCards.add(new Card07(gameBoard));
            case 8:
                characterCards.add(new Card08(gameBoard));
            case 9:
                characterCards.add(new Card09(gameBoard));
            case 10:
                characterCards.add(new Card10(gameBoard));
            case 11:
                characterCards.add(new Card11(gameBoard));
            case 12:
                characterCards.add(new Card12(gameBoard));
        }

        return new GameHandler(gameBoard, new ArrayList<>());
    }

    private GameHandler playAssistantCard(GameHandler gh) throws Exception {
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        while (true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if (gameBoard.getMutableTurnOrder().getSelectedCards().stream()
                    .noneMatch(selected -> selected.getPriority() == finalCard.getPriority())) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }
        return gh;
    }

    @Test(expected = InputValidationException.class)
    public void CharacterCardIndexOutOfBound() throws Exception {
        GameHandler gh = initializeGameBoard(60, 3, 12);
        GameBoard gameBoard = playAssistantCard(gh).getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayCharacterCard playAction = new PlayCharacterCard(player.getId(), 5, Optional.empty(), Optional.empty(), Optional.empty());
        gh.executeAction(playAction);
    }

    @Test
    public void duplicateCharacterCardAction() throws Exception {
        StudentBag studentBag = new StudentBag(24);
        List<PlayerBoard> players = new ArrayList<>(2);
        players.add(new PlayerBoard(1, 2, "Rouge", studentBag));
        players.add(new PlayerBoard(2, 2, "Rampeo", studentBag));
        List<Cloud> clouds = new ArrayList<>(2);
        clouds.add(new Cloud(1));
        clouds.add(new Cloud(2));
        List<CharacterCard> characterCards = new ArrayList<>();
        GameBoard gameBoard = new GameBoard(new IslandField(), GameMode.ADVANCED, studentBag, players, new EnumMap<>(PawnColour.class),
                new TeamMapper(players), new TurnOrder(players.toArray(new PlayerBoard[0])), new EffectTracker(), clouds,
                characterCards, 20, 1);
        gameBoard.refillClouds();
        characterCards.add(new Card01(gameBoard));
        characterCards.add(new Card02(gameBoard));
        characterCards.add(new Card03(gameBoard));
        GameHandler gh = new GameHandler(gameBoard, new ArrayList<>());
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        while (true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if (gameBoard.getMutableTurnOrder().getSelectedCards().stream()
                    .noneMatch(selected -> selected.getPriority() == finalCard.getPriority())) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        StatefulEffect card1 = (StatefulEffect) gameBoard.getCharacterCards().get(0);
        PlayCharacterCard playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.of(0),
                Optional.of((PawnColour) (card1).getState().get(0)),
                Optional.empty());

        gh.executeAction(playCharacterCard);
        try {
            gh.executeAction(playCharacterCard);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: Action\n" +
                    "The error was: this action can't be executed more than once or be executed by other player than the current", exception.getMessage());
        }
    }

    @Test
    public void simpleModePlayAttempt() throws Exception {
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayCharacterCard playAction = new PlayCharacterCard(player.getId(), 5, Optional.empty(), Optional.empty(), Optional.empty());
        try {
            gh.executeAction(playAction);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: Character Card\n" +
                    "The error was: Character Cardcan't be played in simple mode", exception.getMessage());
        }
    }

    @Test
    public void NoAssistantCardPlayed() throws Exception {
        GameHandler gh = new GameHandler(GameMode.ADVANCED, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayCharacterCard playAction = new PlayCharacterCard(player.getId(), 5, Optional.empty(), Optional.empty(), Optional.empty());
        try {
            gh.executeAction(playAction);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: History\n" +
                    "The error was: No PlayAssistantCard has been played", exception.getMessage());
        }
    }

    @Test
    public void InsufficientBalanceException() throws Exception {
        GameHandler gh = initializeGameBoard(20, 1, 2);
        GameBoard gameBoard = playAssistantCard(gh).getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayCharacterCard playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.empty());
        try {
            gh.executeAction(playCharacterCard);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: Character Card\n" +
                    "The error was: Character Card can't be played due to insufficient coin balance", exception.getMessage());
        }
    }
}
