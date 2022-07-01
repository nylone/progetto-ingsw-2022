package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.*;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Misc.OptionalValue;
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

    //create a shared model that will be used by all tests
    final Model model = new Model(GameMode.ADVANCED, "ale", "teo");
    //create a shared controller that will be used by all tests
    final Controller controller = new Controller(new ModelWrapper(model, OptionalValue.empty()), new ArrayList<>());

    @Test
    public void testExecute() throws Exception {
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
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //move 3 student (refer to MoveStudentTest for further information)
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            controller.executeAction(moveStudent);
        }
        //move Mother Nature (refer to MoveMotherNatureTest for further information)
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        controller.executeAction(action);
        //choose cloud (refer to ChooseCloudTest for further information)
        int selectedCloud = Utils.random(model.getClouds()).getId();
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), selectedCloud);
        controller.executeAction(chooseCloudTile);
        //create and execute EndTurnOfActionPhase test
        EndTurnOfActionPhase endTurnOfActionPhase = new EndTurnOfActionPhase(player.getId());
        controller.executeAction(endTurnOfActionPhase);
        //verify that current player has been switched to second player in turn
        assertEquals(model.getMutableTurnOrder().getMutableCurrentPlayer(), model.getMutableTurnOrder().getCurrentTurnOrder().get(1));
    }

    @Test
    public void NoChooseCloudTileTest() throws Exception {
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
        player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        //move 3 student (refer to MoveStudentTest for further information)
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            controller.executeAction(moveStudent);
        }
        //move Mother Nature (refer to MoveMotherNatureTest for further information)
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        controller.executeAction(action);
        //try to end turn without performing chooseCloudTile action
        EndTurnOfActionPhase endTurnOfActionPhase = new EndTurnOfActionPhase(player.getId());
        try {
            controller.executeAction(endTurnOfActionPhase);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: History\n" +
                    "The error was: ChooseCloudTile action has not been executed", exception.getMessage());
        }
    }
}
