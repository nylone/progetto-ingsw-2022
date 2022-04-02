package it.polimi.ingsw.Model;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CharacterDeckGenerator {
    private static final long serialVersionUID = 117L; // convention: 1 for model, (01 -> 99) for objects

    private static List<CharacterCard> deck;

    private CharacterDeckGenerator() {
    }

    public static List<CharacterCard> generateCardSet() {
        if (deck == null) {
            // todo generate cards for the singleton
        }
        Collections.shuffle(deck, new Random(System.currentTimeMillis()));
        return deck.subList(0, 2);
    }
}
