package it.polimi.ingsw.Model;

import org.junit.Test;
public class Card03Test {
    GameBoard gb = new GameBoard(GameMode.ADVANCED, "ari", "teo");
    Card03 card = new Card03(gb);

    @Test
    public void checkUse(){
        CharacterCardInput input = new CharacterCardInput(gb.getPlayerBoardByNickname("teo"));
        input.setTargetIsland(gb.getIslandField().getIslandById(5));
        card.Use(input);

        //error whether the test is executed
    }

}
