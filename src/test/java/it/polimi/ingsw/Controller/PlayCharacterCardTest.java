package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.MoveStudent;
import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Controller.Actions.PlayCharacterCard;
import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.OptionalValue;
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
        //create custom model (card01 will be contained inside model)
        Model model = initializeModel(20, 1, 1);
        Controller controller = initializeControllerAndPlayAssistantCard(model);
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //get model's balance before playing the characterCard
        int initialReserve = model.getCoinReserve();
        //get player's balance before playing the characterCard
        int initialBalance = player.getCoinBalance();
        //get characterCard
        StatefulEffect cardStateful = (StatefulEffect) model.getCharacterCards().get(0);
        //get Pawn from card01's state
        PawnColour fromCard = (PawnColour) (cardStateful).getState().get(0);
        //create playCharacterCard action
        PlayCharacterCard playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.of(0),
                OptionalValue.of((PawnColour) (cardStateful).getState().get(0)),
                OptionalValue.empty());

        controller.executeAction(playCharacterCard);
        //verify that player's balance has been decreased by card01's cost
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        //verify that card01's effect has been applied
        assertTrue(model.getMutableIslandField().getMutableIslandById(0).getStudents().contains(fromCard));
        //verify that coins have returned to model's balance
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        // minus 2 because now card's cost has been increased by one so we keep one coin in the card and return ORIGINAL COST - 1 to the Reserve

        /*
        -------------------------------------------
                PlayCharacterCardTest Card02
         ------------------------------------------
         */
        //create custom model (card02 will be contained inside model)
        model = initializeModel(40, 2, 2);
        controller = initializeControllerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //get model's balance before playing the characterCard
        initialReserve = model.getCoinReserve();
        //get player's balance before playing the characterCard
        initialBalance = player.getCoinBalance();
        //create playCharacterCard action
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.empty());
        controller.executeAction(playCharacterCard);
        //verify that player's balance has been decreased by card02's cost
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        //verify that coins have returned to model's balance
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        //verify that card02's effect has been applied
        assertTrue(model.getMutableEffects().isAlternativeTeacherAssignmentEnabled());

        /*
        -------------------------------------------
                PlayCharacterCardTest Card03
         ------------------------------------------
         */
        //create custom model (card03 will be contained inside model)
        model = initializeModel(60, 3, 3);
        controller = initializeControllerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //get model's balance before playing the characterCard
        initialReserve = model.getCoinReserve();
        //get player's balance before playing the characterCard
        initialBalance = player.getCoinBalance();
        //create playCharacterCard action
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.of(0), OptionalValue.empty(), OptionalValue.empty());
        controller.executeAction(playCharacterCard);
        //create and execute 3 moveStudent actions (refer to moveStudentTest for further information)
        MoveDestination moveDestination = MoveDestination.toIsland(0);
        MoveStudent moveStudent = new MoveStudent(player.getId(), 0, moveDestination);
        controller.executeAction(moveStudent);
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        controller.executeAction(moveStudent);
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 2, moveDestination);
        controller.executeAction(moveStudent);
        //verify that player's balance has been decreased by card03's cost
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        //verify that coins have returned to model's balance
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        //verify that card03's effect has been applied
        assertTrue(model.getInfluencerOf(model.getMutableIslandField().getMutableGroups().get(0)).isEmpty() ||
                model.getInfluencerOf(model.getMutableIslandField().getMutableGroups().get(0)).get().equals(model.getTeamMapper().getTeamID(player)));

         /*
        -------------------------------------------
                PlayCharacterCardTest Card04
         ------------------------------------------
         */
        //create custom model (card04 will be contained inside model)
        model = initializeModel(20, 1, 4);
        controller = initializeControllerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //get model's balance before playing the characterCard
        initialReserve = model.getCoinReserve();
        //get player's balance before playing the characterCard
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.empty());
        controller.executeAction(playCharacterCard);
        //verify that player's balance has been decreased by card04's cost
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        //verify that coins have returned to model's balance
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        //verify that card04's effect has been applied
        assertTrue(model.getMutableEffects().isMotherNatureMovementIncreased());

         /*
        -------------------------------------------
                PlayCharacterCardTest Card05
         ------------------------------------------
         */
        //create custom model (card05 will be contained inside model)
        model = initializeModel(40, 2, 5);
        controller = initializeControllerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //get model's balance before playing the characterCard
        initialReserve = model.getCoinReserve();
        //get player's balance before playing the characterCard
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.of(0), OptionalValue.empty(), OptionalValue.empty());
        controller.executeAction(playCharacterCard);
        //verify that player's balance has been decreased by card05's cost
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        //verify that coins have returned to model's balance
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        //verify that card05's effect has been applied
        cardStateful = (StatefulEffect) model.getCharacterCards().get(0);
        assertEquals(3, cardStateful.getState().size()); // 3 tiles left after one use
        assertEquals(1, model.getMutableIslandField().getMutableIslandGroupById(0).getMutableNoEntryTiles().size()); // the island group contains the NoEntryTile

         /*
        -------------------------------------------
                PlayCharacterCardTest Card06
         ------------------------------------------
         */
        //create custom model (card06 will be contained inside model)
        model = initializeModel(60, 3, 6);
        controller = initializeControllerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //get model's balance before playing the characterCard
        initialReserve = model.getCoinReserve();
        //get player's balance before playing the characterCard
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.empty());
        controller.executeAction(playCharacterCard);
        //verify that player's balance has been decreased by card06's cost
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        //verify that coins have returned to model's balance
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        //verify that card06's effect has been applied
        assertTrue(model.getMutableEffects().isTowerInfluenceDenied());
        /*
        -------------------------------------------
                PlayCharacterCardTest Card07
         ------------------------------------------
         */
        //create custom model (card07 will be contained inside model)
        model = initializeModel(20, 1, 7);
        controller = initializeControllerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //get model's balance before playing the characterCard
        initialReserve = model.getCoinReserve();
        //get player's balance before playing the characterCard
        initialBalance = player.getCoinBalance();
        //create card07's input (refer to CARD07test for further information)
        cardStateful = (StatefulEffect) model.getCharacterCards().get(0);
        List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(player.getEntranceStudents().get(0).get(), (PawnColour) cardStateful.getState().get(0)));
        pairs.add(new Pair<>(player.getEntranceStudents().get(1).get(), (PawnColour) cardStateful.getState().get(1)));
        //create and execute playCharacter card action
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.of(pairs));
        controller.executeAction(playCharacterCard);
        //verify that player's balance has been decreased by card07's cost
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        //verify that coins have returned to model's balance
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        //verify that card07's effect has been applied
        cardStateful = (StatefulEffect) model.getCharacterCards().get(0);
        assertTrue(cardStateful.getState().containsAll(pairs.stream().map(Pair::first).toList()));
        assertTrue(player.getEntranceStudents().containsAll(pairs.stream().map(p -> OptionalValue.of(p.second())).toList()));

         /*
        -------------------------------------------
                PlayCharacterCardTest Card08
         ------------------------------------------
         */
        //create custom model (card08 will be contained inside model)
        model = initializeModel(40, 2, 8);
        controller = initializeControllerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //get model's balance before playing the characterCard
        initialReserve = model.getCoinReserve();
        //get player's balance before playing the characterCard
        initialBalance = player.getCoinBalance();
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.empty());
        controller.executeAction(playCharacterCard);
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        //verify that coins have returned to model's balance
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        //verify that card08's effect has been applied
        assertTrue(model.getMutableEffects().isInfluenceIncreased());

         /*
        -------------------------------------------
                PlayCharacterCardTest Card09
         ------------------------------------------
         */
        //create custom model (card09 will be contained inside model)
        model = initializeModel(60, 3, 9);
        controller = initializeControllerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //get model's balance before playing the characterCard
        initialReserve = model.getCoinReserve();
        //get player's balance before playing the characterCard
        initialBalance = player.getCoinBalance();
        //create card09's input
        PawnColour pawnColour = Utils.random(Arrays.asList(PawnColour.BLUE, PawnColour.RED, PawnColour.YELLOW, PawnColour.GREEN, PawnColour.PINK));
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.empty(), OptionalValue.of(pawnColour), OptionalValue.empty());
        controller.executeAction(playCharacterCard);
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        //verify that coins have returned to model's balance
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        //verify that card09's effect has been applied
        assertTrue(model.getMutableEffects().isPawnColourDenied() && model.getMutableEffects().getDeniedPawnColour().get().equals(pawnColour));

         /*
        -------------------------------------------
                PlayCharacterCardTest Card10
         ------------------------------------------
         */
        //create custom model (card10 will be contained inside model)
        model = initializeModel(20, 2, 10);
        controller = initializeControllerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //get model's balance before playing the characterCard
        initialReserve = model.getCoinReserve();
        //get player's balance before playing the characterCard
        initialBalance = player.getCoinBalance();
        //execute 3 moveStudent actions (refer to MoveStudent test for further information)
        PawnColour firstPawnDiningRoom = player.getEntranceStudents().get(0).get();
        PawnColour secondPawnDiningRoom = player.getEntranceStudents().get(1).get();
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 0, moveDestination);
        controller.executeAction(moveStudent);
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        controller.executeAction(moveStudent);
        moveDestination = MoveDestination.toIsland(1);
        moveStudent = new MoveStudent(player.getId(), 2, moveDestination);
        controller.executeAction(moveStudent);
        //create card10's input (refer to CARD10 test for further information)
        pairs = new ArrayList<>();
        pairs.add(new Pair<>(player.getEntranceStudents().get(4).get(), firstPawnDiningRoom));
        pairs.add(new Pair<>(player.getEntranceStudents().get(5).get(), secondPawnDiningRoom));
        int firstCount = player.getDiningRoomCount(pairs.get(0).first());
        int secondCount = player.getDiningRoomCount(pairs.get(1).first());
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.of(pairs));
        controller.executeAction(playCharacterCard);
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        //verify that coins have returned to model's balance
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        //verify that card10's effect has been applied
        assertTrue(player.getDiningRoomCount(pairs.get(0).first()) >= firstCount - 2 && player.getDiningRoomCount(pairs.get(0).first()) <= firstCount + 2); //equals 2 if students taken from entrance have the same colour
        assertTrue(player.getDiningRoomCount(pairs.get(1).first()) >= secondCount - 2 && player.getDiningRoomCount(pairs.get(1).first()) <= secondCount + 2);
        assertTrue(player.getEntranceStudents().containsAll(pairs.stream().map(p -> OptionalValue.of(p.second())).toList()));

        /*
        -------------------------------------------
                PlayCharacterCardTest Card11
         ------------------------------------------
         */
        //create custom model (card11 will be contained inside model)
        model = initializeModel(40, 2, 11);
        controller = initializeControllerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //get model's balance before playing the characterCard
        initialReserve = model.getCoinReserve();
        //get player's balance before playing the characterCard
        initialBalance = player.getCoinBalance();
        //create card11's input (refer to card11 test for further information)
        cardStateful = (StatefulEffect) model.getCharacterCards().get(0);
        PawnColour chosenPawn = (PawnColour) cardStateful.getState().get(0);
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.empty(), OptionalValue.of(chosenPawn), OptionalValue.empty());
        controller.executeAction(playCharacterCard);
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        //verify that coins have returned to model's balance
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        //verify that card11's effect has been applied
        assertEquals(1, player.getDiningRoomCount(chosenPawn));
        assertEquals(4, cardStateful.getState().size());

        /*
        -------------------------------------------
                PlayCharacterCardTest Card12
         ------------------------------------------
         */
        //create custom model (card12 will be contained inside model)
        model = initializeModel(60, 3, 12);
        controller = initializeControllerAndPlayAssistantCard(model);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //get model's balance before playing the characterCard
        initialReserve = model.getCoinReserve();
        //get player's balance before playing the characterCard
        initialBalance = player.getCoinBalance();
        //create card12's input (refer to card12 test for further information)
        chosenPawn = player.getEntranceStudents().get(1).get();
        moveDestination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        controller.executeAction(moveStudent);
        playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.empty(), OptionalValue.of(chosenPawn), OptionalValue.empty());
        controller.executeAction(playCharacterCard);
        assertEquals(player.getCoinBalance(), initialBalance - model.getCharacterCards().get(0).getCost() + 1);
        //verify that coins have returned to model's balance
        assertEquals(model.getCoinReserve(), initialReserve + model.getCharacterCards().get(0).getCost() - 2);
        //verify that card12's effect has been applied
        assertEquals(0, player.getDiningRoomCount(chosenPawn));


    }

    /**
     * Support method to create a model by using its constructor for debug purposes
     *
     * @param coinReserve   coins available
     * @param coinPerPlayer amount of coins that every player will have
     * @param card          card that will be available in game
     * @return custom model
     */
    private Model initializeModel(int coinReserve, int coinPerPlayer, int card) {
        //create student bag
        StudentBag studentBag = new StudentBag(24);
        //create list of PlayerBoards and add players to it
        List<PlayerBoard> players = new ArrayList<>(2);
        players.add(new PlayerBoard(0, 2, "Rouge", studentBag));
        players.add(new PlayerBoard(1, 2, "Rampeo", studentBag));
        //give coint to all players
        for (PlayerBoard playerBoard : players) {
            playerBoard.setCoinBalance(coinPerPlayer);
        }
        //create list of Clouds and add Clouds to it
        List<Cloud> clouds = new ArrayList<>(2);
        clouds.add(new Cloud(1));
        clouds.add(new Cloud(2));
        //create list of characterCards
        List<CharacterCard> characterCards = new ArrayList<>();
        //create custom model
        Model model = new Model(new IslandField(), GameMode.ADVANCED, studentBag, players, new EnumMap<>(PawnColour.class),
                new TeamMapper(players), new TurnOrder(players), new EffectTracker(), clouds,
                characterCards, coinReserve, coinPerPlayer);
        //refill clouds
        try {
            model.refillClouds();
        } catch (FullContainerException e) {
            throw new RuntimeException(e);
        }
        //add CharacterCard to model (basing on "card" parameter")
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

    /**
     * Support method used to create a Controller and execute SETUP-PHASE
     *
     * @param model model that will be handled by GameHandler
     * @return Controller necessary to execute actions inside tests
     * @throws InputValidationException error related to playAssistantCard actions
     */
    private Controller initializeControllerAndPlayAssistantCard(Model model) throws InputValidationException {
        //create controller
        Controller controller = new Controller(new ModelWrapper(model, OptionalValue.empty()), new ArrayList<>());
        //----- PLAY ASSISTANT CARD (REFER TO PLAYASSISTANTCARD TEST CLASS FOR FURTHER INFORMATION) ----
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
        //create custom model
        Model model = initializeModel(60, 3, 12);
        Controller gh = initializeControllerAndPlayAssistantCard(model);
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //try to play a characterCard with invalid index (<0 || >3)
        PlayCharacterCard playAction = new PlayCharacterCard(player.getId(), 5, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.empty());
        gh.executeAction(playAction);
    }

    @Test
    public void duplicateCharacterCardAction() throws Exception {
        //create custom model
        Model model = initializeModel(20, 3, 1);
        Controller controller = initializeControllerAndPlayAssistantCard(model);
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        StatefulEffect card1 = (StatefulEffect) model.getCharacterCards().get(0);
        PlayCharacterCard playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.of(0),
                OptionalValue.of((PawnColour) (card1).getState().get(0)),
                OptionalValue.empty());
        //try to play a second characterCard in the same turn
        controller.executeAction(playCharacterCard);
        try {
            controller.executeAction(playCharacterCard);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: PlayCharacterCard\n" +
                    "The error was: Too many similar actions have been executed", exception.getMessage());
        }
    }

    @Test
    public void simpleModePlayAttempt() throws Exception {
        //create a model in simple mode
        Model model = new Model(GameMode.SIMPLE, "ale", "teo");
        Controller gh = new Controller(new ModelWrapper(model, OptionalValue.empty()), new ArrayList<>(6));
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayCharacterCard playAction = new PlayCharacterCard(player.getId(), 1, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.empty());
        //try to play a character card in simple mode
        try {
            gh.executeAction(playAction);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: Character Card\n" +
                    "The error was: can't be played in simple mode", exception.getMessage());
        }
    }

    @Test
    public void noAssistantCardPlayed() throws Exception {
        //create model
        Model model = new Model(GameMode.ADVANCED, "ale", "teo");
        Controller gh = new Controller(new ModelWrapper(model, OptionalValue.empty()), new ArrayList<>(6));
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayCharacterCard playAction = new PlayCharacterCard(player.getId(), 5, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.empty());
        //try to play a character card in setup phase
        try {
            gh.executeAction(playAction);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: History\n" +
                    "The error was: the game is not in the correct phase", exception.getMessage());
        }
    }

    @Test
    public void InsufficientBalanceException() throws Exception {
        //create a custom model (players haven't any coin)
        Model model = initializeModel(20, 0, 2);
        Controller gh = initializeControllerAndPlayAssistantCard(model);
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        PlayCharacterCard playCharacterCard = new PlayCharacterCard(player.getId(), 0, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.empty());
        //try to play a character card
        try {
            gh.executeAction(playCharacterCard);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: Character Card\n" +
                    "The error was: can't be played due to insufficient coin balance", exception.getMessage());
        }
    }
}
