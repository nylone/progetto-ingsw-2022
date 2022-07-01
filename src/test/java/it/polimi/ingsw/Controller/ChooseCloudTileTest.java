package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.*;
import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.OptionalValue;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class ChooseCloudTileTest {

    //create a shared model that will be used by all tests
    Model model = new Model(GameMode.SIMPLE, "ale", "teo");

    //create a shared controller that will be used by all tests
    Controller controller = new Controller(new ModelWrapper(model, OptionalValue.empty()), new ArrayList<>());

    /**
     * 2 ChooseCloudAction actions in a row throw an exception because before a ChooseCloudAction should only be present
     * a PlayCharacterCard action or MoveMotherNature action (due to the check order taken in validate method,
     * the controller does not recognize immediately that this action has already been executed, anyway the result
     * is the same that is to say, the validation fails)
     *
     * @throws Exception
     */
    @Test
    public void PreviousActionNotValid() throws Exception {
        PlayerBoard player = model.getMutableTurnOrder().getMutableCurrentPlayer();

        int selectedCloud = Utils.random(model.getClouds()).getId();
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), selectedCloud);
        // try to choose one cloud before moving mother nature
        try {
            controller.executeAction(chooseCloudTile);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: History\n" +
                    "The error was: MoveMotherNature action has not been executed", exception.getMessage());
        }
    }

    @Test
    public void playerCanTakeStudentsFromCloud() throws Exception {
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
            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        //move Mother Nature (refer to MoveMotherNatureTest for further information)
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        controller.executeAction(action);
        //get one cloud randomly and create chooseCloudTile action with it
        int selectedCloud = Utils.random(model.getClouds()).getId();
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), selectedCloud);
        // execute action
        controller.executeAction(chooseCloudTile);
        //verify that cloud has been emptied
        assertEquals(0, model.getClouds().get(selectedCloud).getContents().size());
        //verify that cloud's content has been moved to player's entrance
        assertEquals(0, player.getEntranceSpaceLeft());
    }

    @Test
    public void CloudIndexOutOfBound() throws Exception {
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
            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        //move Mother Nature (refer to MoveMotherNatureTest for further information)
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        controller.executeAction(action);
        // try to play a chooseCloudTile action with an invalid cloud's index
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), 5);
        try {
            controller.executeAction(chooseCloudTile);
        } catch (InvalidElementException exception) {
            assertEquals("An error occurred while validating: Cloud\n" +
                    "The error was: element Cloud was found to be invalid (eg: null, out of bounds or otherwise incorrect).", exception.getMessage());
        }
    }

    @Test
    public void RepeatedActionException() throws Exception {
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
            player = model.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        //move Mother Nature (refer to MoveMotherNatureTest for further information)
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        controller.executeAction(action);
        //create and execute first chooseCloudTile action
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), 0);
        controller.executeAction(chooseCloudTile);
        //create and try to execute second chooseCloudTile action
        chooseCloudTile = new ChooseCloudTile(player.getId(), 1);
        try {
            controller.executeAction(chooseCloudTile);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: ChooseCloudTile\n" +
                    "The error was: Too many similar actions have been executed", exception.getMessage());
        }
    }

    @Test
    public void EmptyIslandException() throws Exception {
        //create custom model without filling first cloud
        StudentBag studentBag = new StudentBag(24);
        List<PlayerBoard> players = new ArrayList<>(2);
        players.add(new PlayerBoard(0, 2, "Rouge", studentBag));
        players.add(new PlayerBoard(1, 2, "Rampeo", studentBag));
        List<Cloud> clouds = new ArrayList<>(2);
        clouds.add(new Cloud(1));
        clouds.add(new Cloud(2));
        clouds.get(1).fill(Arrays.asList(PawnColour.BLUE));
        List<CharacterCard> characterCards = new ArrayList<>();
        Model model = new Model(new IslandField(), GameMode.SIMPLE, studentBag, players, new EnumMap<>(PawnColour.class),
                new TeamMapper(players), new TurnOrder(players), new EffectTracker(), clouds,
                characterCards, 20, 1);
        Controller gh = new Controller(new ModelWrapper(model, OptionalValue.empty()), new ArrayList<>());
        //----- PLAY ASSISTANT CARD (REFER TO PLAYASSISTANTCARD TEST CLASS FOR FURTHER INFORMATION) ----
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
        //move 3 student (refer to MoveStudentTest for further information)
        for (int i = 0; i < 3; i++) {
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
        }
        //move Mother Nature (refer to MoveMotherNatureTest for further information)
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        gh.executeAction(action);
        //try to pick one empty cloud
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), 0);
        try {
            gh.executeAction(chooseCloudTile);
        } catch (GenericInputValidationException exception) {
            assertEquals("An error occurred while validating: Cloud\n" +
                    "The error was: has already been emptied", exception.getMessage());
        }
    }

    @Test
    public void FullEntranceExceptionTest() throws Exception {
        //create custom model without filling first cloud
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
            //add a new student to entrance (entrance will be kept full)
            player.addStudentsToEntrance(Arrays.asList(PawnColour.RED));
        }
        //move Mother Nature (refer to MoveMotherNatureTest for further information)
        int randomMovement = new Random().nextInt(model.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        controller.executeAction(action);
        //try to choose cloud even if entrance has not enough space left
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), 0);
        try {
            controller.executeAction(chooseCloudTile);
        } catch (Exception exception) {
            assertEquals("An error occurred while validating: Entrance\n" +
                    "The error was: can't contain 3 elements without overflowing.", exception.getMessage());
        }
    }
}