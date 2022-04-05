package it.polimi.ingsw.Model;

import java.util.*;
import java.util.stream.Collectors;

public class CharacterDeckGenerator {
    private static final long serialVersionUID = 117L; // convention: 1 for model, (01 -> 99) for objects

    private static List<CharacterCard> deck;

    public static List<CharacterCard> generateCardSet(GameBoard ctx) {
        if (deck == null) {
            CharacterCard[] initdeck = new CharacterCard[]{
                    new Card01(ctx),
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
                    new Card12(ctx),
            };
            deck = Arrays.stream(initdeck).collect(Collectors.toCollection(ArrayList::new));
        }
        Collections.shuffle(deck, new Random(System.currentTimeMillis()));
        return deck.subList(0, 3);
    }
}
