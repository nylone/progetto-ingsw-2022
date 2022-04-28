package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Input.InvalidElementException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChooseCloudTileTest {

    GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");

    @Test
    public void playerCanTakeStudentsFromCloud() throws Exception {
        // arrange
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
        //move 3 pawns
        for(int i=0; i<3; i++){
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            gameBoard = gh.getModelCopy();
            player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        //move Mother Nature
        int randomMovement = new Random().nextInt(gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        gh.executeAction(action);
        gameBoard = gh.getModelCopy();
        // assert
        int selectedCloud = Utils.random(gameBoard.getClouds()).getId();
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), selectedCloud);
        // act
        gh.executeAction(chooseCloudTile);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        // assert
        assertTrue(gameBoard.getClouds().get(selectedCloud).getContents().size() == 0);
        assertTrue(player.getEntranceSpaceLeft() == 0);
    }
    @Test(expected = InputValidationException.class)
    public void EntranceFullException() throws Exception {
        // arrange
        GameHandler gh = new GameHandler(GameMode.SIMPLE, "ale", "teo");
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard currentPlayer = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        ChooseCloudTile action = new ChooseCloudTile(currentPlayer.getId(), 0);
        currentPlayer.removeStudentFromEntrance(6);
        // act
        gh.executeAction(action);
        // assert
    }

    /**
     * 2 ChooseCloudAction actions in a row throw an exception because before a ChooseCloudAction should only be present
     * a PlayCharacterCard action or MoveMotherNature action (due to the check order taken in validate method,
     * the controller does not recognize immediately that this action has already been executed, anyway the result
     * is the same that is to say, the validation fails)
     * @throws Exception
     */
    @Test
    public void PreviousActionNotValid() throws Exception {
        playerCanTakeStudentsFromCloud();
        GameBoard gameBoard = gh.getModelCopy();
        PlayerBoard player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();

        int selectedCloud = Utils.random(gameBoard.getClouds()).getId();
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), selectedCloud);
        // act
        try{
            gh.executeAction(chooseCloudTile);
        }catch(GenericInputValidationException exception){
            assertEquals("An error occurred while validating: History\n" +
                    "The error was: This action can only be executed after a MoveMotherNature action or PlayCharacterCard action", exception.getMessage());
        }
    }
    
    @Test
    public void CloudIndexOutOfBound() throws Exception {
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
        //move 3 pawns
        for(int i=0; i<3; i++){
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            gameBoard = gh.getModelCopy();
            player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        //move Mother Nature
        int randomMovement = new Random().nextInt(gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        gh.executeAction(action);
        // assert
        int selectedCloud = 5;
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), selectedCloud);
        // act
        try{
            gh.executeAction(chooseCloudTile);
        }catch (InvalidElementException exception){
            assertEquals("An error occurred while validating: Cloud\n" +
                    "The error was: element Cloud was found to be invalid (eg: null, out of bounds or otherwise incorrect).", exception.getMessage());
        }
    }

    /** We use the debug version of
     * GameBoard and GameHandler
     *
     * activating PlayCharacterCard action before the second chooseCloudTile action assure to not satisfy the condition
     * checked on PreviousActionNotValid method.
     *
     * @Throws Exception
     */
    @Test
    public void FullEntranceExceptionTest() throws Exception{
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
        characterCards.add(new Card04(gameBoard));
        characterCards.add(new Card09(gameBoard));
        characterCards.add(new Card10(gameBoard));
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
        //move 3 pawns
        for(int i=0; i<3; i++){
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            gameBoard = gh.getModelCopy();
            player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        //move Mother Nature
        int randomMovement = new Random().nextInt(gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        gh.executeAction(action);
        // assert
        int selectedCloud = 0;
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), selectedCloud);

        gh.executeAction(chooseCloudTile);
        gameBoard = gh.getModelCopy();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();

        PlayCharacterCard playCharacterCard = new PlayCharacterCard(player.getId(), 0, Optional.empty(), Optional.empty(), Optional.empty());
        gh.executeAction(playCharacterCard);
        gameBoard = gh.getModelCopy();

        selectedCloud = 1;
        chooseCloudTile = new ChooseCloudTile(player.getId(), selectedCloud);
        try {
            gh.executeAction(chooseCloudTile);
        }catch (GenericInputValidationException exception){
            assertEquals("An error occurred while validating: Entrance\n" +
                    "The error was: Entrancecan't contain 3 element's without overflowing.", exception.getMessage());
        }
    }

    @Test
    public void EmptyIslandException() throws Exception{
        StudentBag studentBag = new StudentBag(24);
        List<PlayerBoard> players = new ArrayList<>(2);
        players.add(new PlayerBoard(1,2,"Rouge", studentBag));
        players.add(new PlayerBoard(2,2,"Rampeo", studentBag));
        List<Cloud> clouds = new ArrayList<>(2);
        clouds.add(new Cloud(1));
        clouds.add(new Cloud(2));
        List<CharacterCard> characterCards = new ArrayList<>();
        GameBoard gameBoard = new GameBoard(new IslandField(),GameMode.SIMPLE,studentBag, players, new EnumMap<>(PawnColour.class),
                new TeamMapper(players), new TurnOrder(players.toArray(new PlayerBoard[0])),new EffectTracker(), clouds,
                characterCards );
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
        //move 3 pawns
        for(int i=0; i<3; i++){
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            gameBoard = gh.getModelCopy();
            player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        //move Mother Nature
        int randomMovement = new Random().nextInt(gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        gh.executeAction(action);
        // assert
        int selectedCloud = 0;
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), selectedCloud);
        try {
            gh.executeAction(chooseCloudTile);
        }catch (GenericInputValidationException exception){
            assertEquals("An error occurred while validating: Cloud\n" +
                    "The error was: Cloud has already been emptied", exception.getMessage());
        }
    }

    @Test
    public void outOfTurnException() throws Exception{
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
        //move 3 pawns
        for(int i=0; i<3; i++){
            MoveDestination moveDestination = MoveDestination.toIsland(0);
            MoveStudent moveStudent = new MoveStudent(player.getId(), i, moveDestination);
            gh.executeAction(moveStudent);
            gameBoard = gh.getModelCopy();
            player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        }
        //move Mother Nature
        int randomMovement = new Random().nextInt(gameBoard.getMutableTurnOrder().getMutableSelectedCard(player).get().getMaxMovement());
        randomMovement = randomMovement == 0 ? 1 : randomMovement;;
        PlayerAction action = new MoveMotherNature(player.getId(), randomMovement);
        // act
        gh.executeAction(action);
        gameBoard = gh.getModelCopy();
        // assert
        int selectedCloud = Utils.random(gameBoard.getClouds()).getId();

        // act
        gameBoard.getMutableTurnOrder().stepToNextPlayer();
        player = gameBoard.getMutableTurnOrder().getMutableCurrentPlayer();
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(player.getId(), selectedCloud);
        try {
            gh.executeAction(chooseCloudTile);
        }catch (Exception exception){
            assertEquals("An error occurred while validating: Action\n" +
                    "The error was: this action can't be executed more than once or be executed by other player than the current", exception.getMessage());
        }
    }
}