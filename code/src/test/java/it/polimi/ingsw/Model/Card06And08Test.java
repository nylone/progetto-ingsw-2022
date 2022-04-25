package it.polimi.ingsw.Model;

import static org.junit.Assert.*;

import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

public class Card06And08Test{
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card06 card06 = new Card06(gb);
    Card08 card08 = new Card08(gb);
    PlayerBoard pb = new PlayerBoard(1, 2, "ari", gb.getStudentBag());
    @Test
    public void checkUse06(){
        CharacterCardInput input = new CharacterCardInput(pb);
        card06.Use(input);

        assertTrue(gb.getEffects().isTowerInfluenceDenied());
    }
    @Test
    public void checkUse08() throws InvalidContainerIndexException {
        Island island = gb.getIslandField().getIslandGroupById(1).getIslands().get(0);
        IslandGroup islandGroup = gb.getIslandField().getIslandGroupById(0);
        PlayerBoard pb2 = gb.getPlayerBoardByNickname("teo");
        TurnOrder turnOrder = gb.getTurnOrder();
        island.addStudent(PawnColour.RED);
        gb.setTeacher(PawnColour.RED, pb2);

        turnOrder.setSelectedCard(turnOrder.getCurrentPlayer(),turnOrder.getCurrentPlayer().getAssistantCards()[1], turnOrder.getCurrentPlayer().getAssistantCards());
        turnOrder.stepToNextPlayer();
        turnOrder.setSelectedCard(turnOrder.getCurrentPlayer(), turnOrder.getCurrentPlayer().getAssistantCards()[0], turnOrder.getCurrentPlayer().getAssistantCards());
        turnOrder.stepToNextPlayer();

        CharacterCardInput input = new CharacterCardInput(turnOrder.getCurrentPlayer());
        card08.Use(input);

        assertTrue(gb.influencerOf(islandGroup).get().equals(gb.getTeamMap().getTeamID(turnOrder.getCurrentPlayer())));
        /*in this test, adding 2 points due to card8's effect will always give the influence to the caller; in fact:
                teo caller --> 3 points of influence teo / 0 points of influence ari
                ari caller --> 2 points of influence ari / 1 points of influence teo
         */
    }
}
