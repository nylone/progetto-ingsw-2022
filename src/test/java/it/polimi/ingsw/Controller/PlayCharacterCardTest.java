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
        Model model = initializeGameBoard(20, 1, 1);
        Controller gh = initializeGameHandlerAndPlayAssistantCard(model);
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        int initialReserve = model.getCoinReserve();
        int initialBalance = player.getCoinBalance();
        StatefulEffect cardStateful = (StatefulEffect) model.getCharacterCards().get(0);
        PawnColour fromCard = (PawnColour) (cardStateful).getState().get(0);
        PlayCharacterCard playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.of(0),
                Optional.of((PawnColour) (cardStateful).getState().get(0)),
                Optional.empty());

        gh.executeAction(playCharacterCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        assertTrue(model.getMutableIslandField().getMutableIslandById(0).getStudents().contains(fromCard));
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        // minus 2 because now card's cost has been increased by one so we keep one coin in the card and return ORIGINAL COST - 1 to the Reserve

        /*
        -------------------------------------------
                PlayCharacterCardTest Card02
         ------------------------------------------
         */
        model = initializeGameBoard(40, 2, 2);
        gh = initializeGameHandlerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = model.getCoinReserve();
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        assertTrue(model.getMutableEffects().isAlternativeTeacherAssignmentEnabled());

        /*
        -------------------------------------------
                PlayCharacterCardTest Card03
         ------------------------------------------
         */
        model = initializeGameBoard(60, 3, 3);
        gh = initializeGameHandlerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = model.getCoinReserve();
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.of(0), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        MoveDestination moveDestination = MoveDestination.toIsland(0);
        MoveStudent moveStudent = new MoveStudent(player.getId(), 0, moveDestination);
        gh.executeAction(moveStudent);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        gh.executeAction(moveStudent);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 2, moveDestination);
        gh.executeAction(moveStudent);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        assertTrue(model.getInfluencerOf(model.getMutableIslandField().getMutableGroups().get(0)).isEmpty() ||
                model.getInfluencerOf(model.getMutableIslandField().getMutableGroups().get(0)).get().equals(model.getTeamMapper().getTeamID(player)));

         /*
        -------------------------------------------
                PlayCharacterCardTest Card04
         ------------------------------------------
         */
        model = initializeGameBoard(20, 1, 4);
        gh = initializeGameHandlerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = model.getCoinReserve();
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        assertTrue(model.getMutableEffects().isMotherNatureMovementIncreased());

         /*
        -------------------------------------------
                PlayCharacterCardTest Card05
         ------------------------------------------
         */
        model = initializeGameBoard(40, 2, 5);
        gh = initializeGameHandlerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = model.getCoinReserve();
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.of(0), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        cardStateful = (StatefulEffect) model.getCharacterCards().get(0);
        assertEquals(3, cardStateful.getState().size()); // 3 tiles left after one use
        assertEquals(1, model.getMutableIslandField().getMutableIslandGroupById(0).getMutableNoEntryTiles().size()); // the island group contains the NoEntryTile

         /*
        -------------------------------------------
                PlayCharacterCardTest Card06
         ------------------------------------------
         */
        model = initializeGameBoard(60, 3, 6);
        gh = initializeGameHandlerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = model.getCoinReserve();
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        assertTrue(model.getMutableEffects().isTowerInfluenceDenied());
        /*
        -------------------------------------------
                PlayCharacterCardTest Card07
         ------------------------------------------
         */
        model = initializeGameBoard(20, 1, 7);
        gh = initializeGameHandlerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = model.getCoinReserve();
        initialBalance = player.getCoinBalance();
        cardStateful = (StatefulEffect) model.getCharacterCards().get(0);
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(player.getEntranceStudents().get(0).get(), (PawnColour) cardStateful.getState().get(0)));
        pairs.add(new Pair<>(player.getEntranceStudents().get(1).get(), (PawnColour) cardStateful.getState().get(1)));
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.of(pairs));
        gh.executeAction(playCharacterCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        cardStateful = (StatefulEffect) model.getCharacterCards().get(0);
        assertTrue(cardStateful.getState().containsAll(pairs.stream().map(Pair::getFirst).toList()));
        assertTrue(player.getEntranceStudents().containsAll(pairs.stream().map(p -> Optional.of(p.getSecond())).toList()));

         /*
        -------------------------------------------
                PlayCharacterCardTest Card08
         ------------------------------------------
         */
        model = initializeGameBoard(40, 2, 8);
        gh = initializeGameHandlerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = model.getCoinReserve();
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        assertTrue(model.getMutableEffects().isInfluenceIncreased());

         /*
        -------------------------------------------
                PlayCharacterCardTest Card09
         ------------------------------------------
         */
        model = initializeGameBoard(60, 3, 9);
        gh = initializeGameHandlerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = model.getCoinReserve();
        initialBalance = player.getCoinBalance();
        PawnColour pawnColour = Utils.random(Arrays.asList(PawnColour.BLUE, PawnColour.RED, PawnColour.YELLOW, PawnColour.GREEN, PawnColour.PINK));
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.of(pawnColour), Optional.empty());
        gh.executeAction(playCharacterCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        assertTrue(model.getMutableEffects().isPawnColourDenied() && model.getMutableEffects().getDeniedPawnColour().get().equals(pawnColour));

         /*
        -------------------------------------------
                PlayCharacterCardTest Card10
         ------------------------------------------
         */
        model = initializeGameBoard(20, 2, 10);
        gh = initializeGameHandlerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = model.getCoinReserve();
        initialBalance = player.getCoinBalance();
        PawnColour firstPawnDiningRoom = player.getEntranceStudents().get(0).get();
        PawnColour secondPawnDiningRoom = player.getEntranceStudents().get(1).get();
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 0, moveDestination);
        gh.executeAction(moveStudent);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        gh.executeAction(moveStudent);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        moveDestination = MoveDestination.toIsland(1);
        moveStudent = new MoveStudent(player.getId(), 2, moveDestination);
        gh.executeAction(moveStudent);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        pairs = new ArrayList<>();
        pairs.add(new Pair<>(player.getEntranceStudents().get(4).get(), firstPawnDiningRoom));
        pairs.add(new Pair<>(player.getEntranceStudents().get(5).get(), secondPawnDiningRoom));
        int firstCount = player.getDiningRoomCount(pairs.get(0).getFirst());
        int secondCount = player.getDiningRoomCount(pairs.get(1).getFirst());
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.of(pairs));
        gh.executeAction(playCharacterCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        assertTrue(player.getDiningRoomCount(pairs.get(0).getFirst()) >= firstCount - 2 && player.getDiningRoomCount(pairs.get(0).getFirst()) <= firstCount + 2); //equals 2 if students taken from entrance have the same colour
        assertTrue(player.getDiningRoomCount(pairs.get(1).getFirst()) >= secondCount - 2 && player.getDiningRoomCount(pairs.get(1).getFirst()) <= secondCount + 2);
        assertTrue(player.getEntranceStudents().containsAll(pairs.stream().map(p -> Optional.of(p.getSecond())).toList()));

        /*
        -------------------------------------------
                PlayCharacterCardTest Card11
         ------------------------------------------
         */
        model = initializeGameBoard(40, 2, 11);
        gh = initializeGameHandlerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = model.getCoinReserve();
        initialBalance = player.getCoinBalance();
        cardStateful = (StatefulEffect) model.getCharacterCards().get(0);
        PawnColour chosenPawn = (PawnColour) cardStateful.getState().get(0);
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.of(chosenPawn), Optional.empty());
        gh.executeAction(playCharacterCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        assertEquals(1, player.getDiningRoomCount(chosenPawn));
        assertEquals(4, cardStateful.getState().size());

        /*
        -------------------------------------------
                PlayCharacterCardTest Card12
         ------------------------------------------
         */
        model = initializeGameBoard(60, 3, 12);
        gh = initializeGameHandlerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        initialReserve = model.getCoinReserve();
        initialBalance = player.getCoinBalance();
        chosenPawn = player.getEntranceStudents().get(1).get();
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        gh.executeAction(moveStudent);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.of(chosenPawn), Optional.empty());
        gh.executeAction(playCharacterCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        assertEquals(0, player.getDiningRoomCount(chosenPawn));


    }

    private Model initializeGameBoard(int coinReserve, int coinPerPlayer, int card) {
        StudentBag studentBag = new StudentBag(24);
        List<PlayerBoard> players = new ArrayList<>(2);
        players.add(new PlayerBoard(0, 2, "Rouge", studentBag));
        players.add(new PlayerBoard(1, 2, "Rampeo", studentBag));
        for (PlayerBoard playerBoard : players) {
            playerBoard.setCoinBalance(coinPerPlayer);
        }
        List<Cloud> clouds = new ArrayList<>(2);
        clouds.add(new Cloud(1));
        clouds.add(new Cloud(2));
        List<CharacterCard> characterCards = new ArrayList<>();
        Model model = new Model(new IslandField(), GameMode.ADVANCED, studentBag, players, new EnumMap<>(PawnColour.class),
                new TeamMapper(players), new TurnOrder(players), new EffectTracker(), clouds,
                characterCards, coinReserve, coinPerPlayer);
        model.refillClouds();
        switch (card) {
            case 1:
                characterCards.add(new Card01(model));
            case 2:
                characterCards.add(new Card02(model));
            case 3:
                characterCards.add(new Card03(model));
            case 4:
                characterCards.add(new Card04(model));
            case 5:
                characterCards.add(new Card05(model));
            case 6:
                characterCards.add(new Card06(model));
            case 7:
                characterCards.add(new Card07(model));
            case 8:
                characterCards.add(new Card08(model));
            case 9:
                characterCards.add(new Card09(model));
            case 10:
                characterCards.add(new Card10(model));
            case 11:
                characterCards.add(new Card11(model));
            case 12:
                characterCards.add(new Card12(model));
        }
        return model;

    }

    private Controller initializeGameHandlerAndPlayAssistantCard(Model model) throws InputValidationException {
        Controller controller = new Controller(new ModelWrapper(model, null), new ArrayList<>());
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        controller.executeAction(playAssistantCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();

        while (true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if (model.getMutableTurnOrder().getSelectedCards().stream()
                    .noneMatch(selected -> selected.getPriority() == finalCard.getPriority())) {
                controller.executeAction(playAssistantCard1);
                break;
            }
        }
        return controller;
    }

    @Test(expected = InputValidationException.class)
    public void CharacterCardIndexOutOfBound() throws Exception {
        Model model = initializeGameBoard(60, 3, 12);
        Controller gh = initializeGameHandlerAndPlayAssistantCard(model);
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayCharacterCard playAction = new PlayCharacterCard(player.getId(), 5, Optional.empty(), Optional.empty(), Optional.empty());
        gh.executeAction(playAction);
    }

    @Test
    public void duplicateCharacterCardAction() throws Exception {
        StudentBag studentBag = new StudentBag(24);
        List<PlayerBoard> players = new ArrayList<>(2);
        players.add(new PlayerBoard(0, 2, "Rouge", studentBag));
        players.add(new PlayerBoard(1, 2, "Rampeo", studentBag));
        List<Cloud> clouds = new ArrayList<>(2);
        clouds.add(new Cloud(1));
        clouds.add(new Cloud(2));
        List<CharacterCard> characterCards = new ArrayList<>();
        Model model = new Model(new IslandField(), GameMode.ADVANCED, studentBag, players, new EnumMap<>(PawnColour.class),
                new TeamMapper(players), new TurnOrder(players), new EffectTracker(), clouds,
                characterCards, 20, 1);
        model.refillClouds();
        characterCards.add(new Card01(model));
        characterCards.add(new Card02(model));
        characterCards.add(new Card03(model));
        Controller gh = new Controller(new ModelWrapper(model, null), new ArrayList<>());
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        AssistantCard card = Utils.random(player.getMutableAssistantCards());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(player.getId(), card.getPriority());
        gh.executeAction(playAssistantCard);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        while (true) {
            card = Utils.random(player.getMutableAssistantCards());
            PlayAssistantCard playAssistantCard1 = new PlayAssistantCard(player.getId(), card.getPriority());
            AssistantCard finalCard = card;
            if (model.getMutableTurnOrder().getSelectedCards().stream()
                    .noneMatch(selected -> selected.getPriority() == finalCard.getPriority())) {
                gh.executeAction(playAssistantCard1);
                break;
            }
        }
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        StatefulEffect card1 = (StatefulEffect) model.getCharacterCards().get(0);
        PlayCharacterCard playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.of(0),
                Optional.of((PawnColour) (card1).getState().get(0)),
                Optional.empty());

        gh.executeAction(playCharacterCard);
        try {
            gh.executeAction(playCharacterCard);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: PlayCharacterCard\n" +
                    "The error was: Too many similar actions have been executed", exception.getMessage());
        }
    }

    @Test
    public void simpleModePlayAttempt() throws Exception {
        Model model = new Model(GameMode.SIMPLE, "ale", "teo");
        Controller gh = new Controller(new ModelWrapper(model, null), new ArrayList<>(6));
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayCharacterCard playAction = new PlayCharacterCard(player.getId(), 5, Optional.empty(), Optional.empty(), Optional.empty());
        try {
            gh.executeAction(playAction);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: Character Card\n" +
                    "The error was: Character Card can't be played in simple mode", exception.getMessage());
        }
    }

    @Test
    public void noAssistantCardPlayed() throws Exception {
        Model model = new Model(GameMode.SIMPLE, "ale", "teo");
        Controller gh = new Controller(new ModelWrapper(model, null), new ArrayList<>(6));
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayCharacterCard playAction = new PlayCharacterCard(player.getId(), 5, Optional.empty(), Optional.empty(), Optional.empty());
        try {
            gh.executeAction(playAction);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: Character Card\n" +
                    "The error was: Character Card can't be played in simple mode", exception.getMessage());
        }
    }

    @Test
    public void InsufficientBalanceException() throws Exception {
        Model model = initializeGameBoard(20, 0, 2);
        Controller gh = initializeGameHandlerAndPlayAssistantCard(model);
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayCharacterCard playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.empty());
        try {
            gh.executeAction(playCharacterCard);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: Character Card\n" +
                    "The error was: Character Card can't be played due to insufficient coin balance", exception.getMessage());
        }
    }
}
