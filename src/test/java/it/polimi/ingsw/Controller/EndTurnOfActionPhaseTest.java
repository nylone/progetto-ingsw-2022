package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.*;
import it.polimi.ingsw.Controller.Enums.MoveDestination;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.ModelWrapper;
import it.polimi.ingsw.Model.PlayerBoard;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;


public class EndTurnOfActionPhaseTest {

    Model model = new Model(GameMode.ADVANCED, "ale", "teo");
    Controller gh = new Controller(new ModelWrapper(model, null), new ArrayList<>());

    @Test
    public void testExecute() throws Exception {
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
        // move 3 pawns
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        // move Mother Nature
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        gh.executeAction(action);
        // assert
        int selectedCloud = Utils.random(model.getClouds()).getId();
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), selectedCloud);
        // act
        gh.executeAction(chooseCloudTile);
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();

        EndTurnOfActionPhase endTurnOfActionPhase = new EndTurnOfActionPhase(player.getId());
        gh.executeAction(endTurnOfActionPhase);
        // assert
        assertEquals(model.getClouds().stream().filter(cloud -> cloud.getContents().size() == 3).count(), model.getClouds().size() - 1);
    }

    @Test
    public void NoChooseCloudTileTest() throws Exception {
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
        // move 3 pawns
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        // move Mother Nature
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        gh.executeAction(action);

        EndTurnOfActionPhase endTurnOfActionPhase = new EndTurnOfActionPhase(player.getId());
        try {
            gh.executeAction(endTurnOfActionPhase);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: History\n" +
                    "The error was: ChooseCloudTile action has not been executed", exception.getMessage());
        }
    }

    @Test
    public void OutOfTurnAction() throws Exception {
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
        // move 3 pawns
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        // move Mother Nature
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        gh.executeAction(action);
        // assert
        int selectedCloud = Utils.random(model.getClouds()).getId();
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), selectedCloud);
        // act
        gh.executeAction(chooseCloudTile);
        model.getMutableTurnOrder().stepToNextPlayer();
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();

        EndTurnOfActionPhase endTurnOfActionPhase = new EndTurnOfActionPhase(player.getId());
        try {
            gh.executeAction(endTurnOfActionPhase);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: Action\n" +
                    "The error was: this action can't be executed more than once or be executed by other player than the current", exception.getMessage());
        }
    }
}
