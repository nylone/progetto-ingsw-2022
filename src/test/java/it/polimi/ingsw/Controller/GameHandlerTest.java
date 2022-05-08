package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class GameHandlerTest {
    @Test
    public void finishingTowersMakesWin() throws InputValidationException {
        // arrange & act
        GameBoard gb = new GameBoard(GameMode.SIMPLE, "ale", "teo", "ari");
        PlayerBoard currentPlayer = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        int expectedWinningTeam = gb.getTeamMap().getTeamID(currentPlayer).getTeamID();
        for (int i = 0; i < 5; i++) { // leaving current player with just one tower left
            gb.getTeamMap().getMutableTowerStorage(currentPlayer).extractTower();
        }

        GameHandler gh = new GameHandler(gb, new ArrayList<>(6));
        PlayerAction action = new PlayAssistantCard(currentPlayer.getId(), 3);
        gh.executeAction(action);

        // next player chooses card
        action = new PlayAssistantCard(gb.getMutableTurnOrder().getMutableCurrentPlayer().getId(), 5);
        gh.executeAction(action);
        currentPlayer = gb.getMutableTurnOrder().getMutableCurrentPlayer();

        // next player chooses card
        action = new PlayAssistantCard(gb.getMutableTurnOrder().getMutableCurrentPlayer().getId(), 7);
        gh.executeAction(action);
        currentPlayer = gb.getMutableTurnOrder().getMutableCurrentPlayer();


        for (int i = 0; i < 4; i++) {
            PlayerAction a = new MoveStudent(currentPlayer.getId(), i+1, MoveDestination.toDiningRoom());
            gh.executeAction(a);
        }

        IslandGroup afterMnIsland = Utils.modularSelection(
                gb.getMutableIslandField().getMutableMotherNaturePosition(),
                gb.getMutableIslandField().getMutableGroups(),
                1); // selecting island after the island containing mother nature
        PawnColour studentOnIsland = afterMnIsland.getStudents().get(0); // checking student colour on selected island
        gb.setTeacher(studentOnIsland, currentPlayer);

        PlayerAction mnAction = new MoveMotherNature(currentPlayer.getId(), 1);
        gh.executeAction(mnAction);
        // assert
        assertTrue(expectedWinningTeam == gh.getWinner().get());
    }

    @Test
    public void randomWinnerWhenSameNumberOfTowersAndTeachers() throws InputValidationException, FullContainerException, InvalidContainerIndexException {
        // arrange & act
        IslandField field = new IslandField();
        IslandGroup mn = field.getMutableMotherNaturePosition();
        IslandGroup ig1 = Utils.modularSelection(mn, field.getMutableGroups(), 1);
        PawnColour st1 = ig1.getStudents().get(0);
        int offsetIg2 = 0;
        for (int i = 1; i < 6; i++) {
            IslandGroup ig = Utils.modularSelection(ig1, field.getMutableGroups(), i);
            if (!ig.getStudents().get(0).equals(st1)) {
                offsetIg2 = i;
                break;
            }
        }
        IslandGroup ig2 = Utils.modularSelection(ig1, field.getMutableGroups(), offsetIg2);
        PawnColour st2 = ig2.getStudents().get(0);
        StudentBag bag = new StudentBag(5);
        PlayerBoard p1 = new PlayerBoard(1, 2, "ale", bag);
        PlayerBoard p2 = new PlayerBoard(2, 2, "teo", bag);
        p1.removeStudentFromEntrance(0);
        p2.removeStudentFromEntrance(0);
        p1.addStudentsToEntrance(List.of(st1));
        p2.addStudentsToEntrance(List.of(st2));
        Cloud c1 = new Cloud(0);
        Cloud c2 = new Cloud(1);
        c1.fill((ArrayList<PawnColour>) bag.multipleExtraction(3));
        c2.fill((ArrayList<PawnColour>) bag.multipleExtraction(3));
        GameBoard gb = new GameBoard(
                field,
                GameMode.SIMPLE,
                bag,
                new ArrayList<PlayerBoard>() {{ add(p1); add(p2); }},
                new HashMap<PawnColour, PlayerBoard>(),
                new TeamMapper(List.of(p1, p2)),
                new TurnOrder(p1, p2),
                new EffectTracker(),
                new ArrayList<Cloud>() {{ add(c1); add(c2); }},
                null,
                0,
                0);

        PlayerBoard currentPlayer = gb.getMutableTurnOrder().getMutableCurrentPlayer();
        int bagSize = gb.getMutableStudentBag().getSize();
        for (int i = 0; i < bagSize; i++) { // emptying bag
            gb.getMutableStudentBag().extract();
        }

        GameHandler gh = new GameHandler(gb, new ArrayList<>(6));
        PlayerAction action = new PlayAssistantCard(currentPlayer.getId(), 3);
        gh.executeAction(action);

        // next player chooses card
        action = new PlayAssistantCard(gb.getMutableTurnOrder().getMutableCurrentPlayer().getId(), 10);
        gh.executeAction(action);

        for (int i = 0; i < 2; i++) {
            PlayerAction a = new MoveStudent(currentPlayer.getId(), i+1, MoveDestination.toIsland(11));
            gh.executeAction(a);
        }

        PlayerAction a = new MoveStudent(currentPlayer.getId(),
                0,
                MoveDestination.toDiningRoom()); // gaining teacher of the pawn colour on the island
        gh.executeAction(a);

        PlayerAction mnAction = new MoveMotherNature(currentPlayer.getId(), 1);
        gh.executeAction(mnAction);

        PlayerAction cloudAction = new ChooseCloudTile(currentPlayer.getId(), 0);
        gh.executeAction(cloudAction);
        PlayerAction endAction = new EndTurnOfActionPhase(currentPlayer.getId());
        gh.executeAction(endAction);
        assertTrue(gh.getWinner().isEmpty());

        currentPlayer = gb.getMutableTurnOrder().getMutableCurrentPlayer();

        for (int i = 0; i < 2; i++) {
            PlayerAction a2 = new MoveStudent(currentPlayer.getId(), i+1, MoveDestination.toIsland(11));
            gh.executeAction(a2);
        }

        PlayerAction a2 = new MoveStudent(currentPlayer.getId(),
                0,
                MoveDestination.toDiningRoom()); // gaining teacher of the pawn colour on the island
        gh.executeAction(a2);

        PlayerAction mnAction2 = new MoveMotherNature(currentPlayer.getId(), offsetIg2);
        gh.executeAction(mnAction2);

        PlayerAction cloudAction2 = new ChooseCloudTile(currentPlayer.getId(), 1);
        gh.executeAction(cloudAction2);
        assertTrue(gh.getWinner().isEmpty());
        PlayerAction endAction2 = new EndTurnOfActionPhase(currentPlayer.getId());
        gh.executeAction(endAction2);
        int winningTeam = gh.getWinner().get();
        assertTrue(winningTeam == 0 || winningTeam == 1);
    }
}
