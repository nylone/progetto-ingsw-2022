package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.MoveStudent;
import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.OptionalValue;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.ModelWrapper;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class MoveStudentTest {
    //create a shared model that will be used by all tests
    Model model = new Model(GameMode.ADVANCED, "ale", "teo");
    //create a shared controller that will be used by all tests
    Controller controller = new Controller(new ModelWrapper(model, OptionalValue.empty()), new ArrayList<>());

    @Test
    public void testSingleMovementToIsland() throws Exception {
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

        //get current player
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //select island as student's destination
        MoveDestination moveDestination = MoveDestination.toIsland(0);
        //get pawn from player's entrance
        PawnColour toMove = player.getEntranceStudents().get(1).get();
        //get the count of pawnColour on the island before moveStudent action
        int previousCount = (int) model.getMutableIslandField().getMutableIslandById(0).getStudents()
                .stream().filter(pawnColour -> toMove == pawnColour).count();
        //create and execute moveStudent action
        MoveStudent moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        controller.executeAction(moveStudent);
        //get the count of pawnColour on the island after moveStudent action
        int updatedCount = (int) model.getMutableIslandField().getMutableIslandById(0).getStudents()
                .stream().filter(pawnColour -> toMove == pawnColour).count();
        //check that previousCount has only increased by one
        assertEquals(updatedCount, previousCount + 1);
    }

    @Test
    public void testSingleMovementToDiningRoom() throws Exception {
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

        //get current player
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //select diningRoom as student's destination
        MoveDestination destination = MoveDestination.toDiningRoom();
        //get pawn from player's entrance
        PawnColour toMove = player.getEntranceStudents().get(0).get();
        //get the count of pawnColour in the diningRoom before moveStudent action
        int previousCount = player.getDiningRoomCount(toMove);
        //create and execute moveStudent action
        MoveStudent moveStudent = new MoveStudent(player.getId(), 0, destination);
        controller.executeAction(moveStudent);

        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //check that previousCount has only increased by one
        assertEquals(player.getDiningRoomCount(toMove), previousCount + 1);
    }

    @Test
    public void Max3MovementsWithoutCharacterCardTest() throws Exception {
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
        //execute 3 moveStudent actions
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        MoveDestination destination = MoveDestination.toDiningRoom();
        MoveStudent moveStudent = new MoveStudent(player.getId(), 0, destination);
        controller.executeAction(moveStudent);
        destination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 1, destination);
        controller.executeAction(moveStudent);
        destination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 2, destination);
        controller.executeAction(moveStudent);
        destination = MoveDestination.toDiningRoom();
        moveStudent = new MoveStudent(player.getId(), 3, destination);
        //fourth move student action will throw an exception
        try {
            controller.executeAction(moveStudent); //exception
        } catch (InputValidationException exception) {
            assertEquals("An error occurred while validating: History\n" +
                    "The error was: only 3 pawns can be moved from entrance", exception.getMessage());
        }
    }

    @Test
    public void testMovementWithoutAssistantCard() {
        //create and execute moveStudent
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        MoveDestination moveDestination = MoveDestination.toIsland(0);
        MoveStudent moveStudent = new MoveStudent(player.getId(), 1, moveDestination);

        try {
            controller.executeAction(moveStudent);
        } catch (InputValidationException exception) {
            assertEquals("An error occurred while validating: GamePhase\n" +
                    "The error was: the game is not in the correct phase", exception.getMessage());
        }
    }

    @Test(expected = InvalidElementException.class)
    public void InvalidPlayerBoardIndexException() throws Exception {
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
        //create a moveStudent action with invalid playerBoard's id
        MoveDestination destination = MoveDestination.toDiningRoom();
        MoveStudent moveStudent = new MoveStudent(3, 0, destination);
        controller.executeAction(moveStudent);
    }

    @Test(expected = InvalidElementException.class)
    public void InvalidEntranceIndexException() throws Exception {
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
        //create a moveStudent action with invalid entrance position
        MoveDestination destination = MoveDestination.toDiningRoom();
        MoveStudent moveStudent = new MoveStudent(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(), 12, destination);
        controller.executeAction(moveStudent);
    }

    @Test(expected = InvalidElementException.class)
    public void InvalidIslandIdException() throws Exception {
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
        //create a moveStudent action with invalid island'id
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        MoveDestination moveDestination = MoveDestination.toIsland(15);
        MoveStudent moveStudent = new MoveStudent(player.getId(), 1, moveDestination);
        controller.executeAction(moveStudent);
    }

    @Test
    public void SelectedEntrancePositionEmpty() throws Exception {
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
        //execute first moveStudent action
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        MoveDestination destination = MoveDestination.toDiningRoom();
        MoveStudent moveStudent = new MoveStudent(player.getId(), 0, destination);
        controller.executeAction(moveStudent);
        //create a moveStudent action with same entrance's position as the first moveStudent action
        moveStudent = new MoveStudent(player.getId(), 0, destination);

        try {
            controller.executeAction(moveStudent);
        } catch (InvalidElementException exception) {
            assertEquals("An error occurred while validating: Target Entrance Position\n" +
                    "The error was: element Target Entrance Position was found to be invalid (eg: null, out of bounds or otherwise incorrect).", exception.getMessage());
        }

    }


}
