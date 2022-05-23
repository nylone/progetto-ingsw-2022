package it.polimi.ingsw.Model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CharacterDeckGenerator {
    public static List<CharacterCard> generateCardSet(GameBoard ctx) {
        List<CharacterCard> fulldeck = Arrays.asList(new Card01(ctx),
                new Card02(ctx),
                new Card03(ctx),
                new Card04(ctx),
                new Card05(ctx),
                new Card06(ctx),
                new Card07(ctx),
                new Card08(ctx),
                new Card09(ctx),
                new Card10(ctx),
                new Card11(ctx),
                new Card12(ctx));
        Collections.shuffle(fulldeck, new Random(System.currentTimeMillis()));
        return List.copyOf(fulldeck.subList(0, 3));
    }
}
